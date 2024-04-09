package com.example.service;

import com.example.dao.CostDAO;
import com.example.dao.SlotVehicleDAO;
import com.example.dao.SlotsDAO;
import com.example.dto.SlotVehicleDTO;
import com.example.dto.VehicleDTO;
import com.example.entity.custom.Cost;
import com.example.entity.custom.SlotVehicle;
import com.example.entity.custom.Slots;
import com.example.util.*;
import org.modelmapper.ModelMapper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SlotVehicleService {
    private SlotVehicleDAO slotVehicleDAO;
    private SlotsDAO slotsDAO;
    private CostDAO costDAO;

    public SlotVehicleService(){
        this.slotVehicleDAO = new SlotVehicleDAO();
        this.slotsDAO = new SlotsDAO();
        this.costDAO = new CostDAO();
    }

    public boolean isUserAlreadyParkedVehicle(String userId) {
        return slotVehicleDAO.isUserAlreadyParkedVehicle(userId)!=null;
    }

    public SlotVehicleDTO getExistingParkedDetails(String userId){
        SlotVehicle et = slotVehicleDAO.isUserAlreadyParkedVehicle(userId);
        SlotVehicleDTO map = new ModelMapper().map(et, SlotVehicleDTO.class);
        return map;
    }

    public double calculateFee(int slotId, UserType type, Time time){
        Slots slotById = slotsDAO.getSlotById(slotId);
        double u_p = type == UserType.STUDENT ? 60 :
                     type == UserType.LECTURE ? 90 :
                     type == UserType.VISITOR ? 100 : 160;
        double s_p = slotById.getType() == VehicleType.REGULAR ? 10 :
                     slotById.getType() == VehicleType.ELECTRIC ? 20 : 30;
        LocalTime now = LocalTime.now();
        Time time1 = Time.valueOf(now);
        long timeDifferenceMillis = time1.getTime() - time.getTime();

        long total = timeDifferenceMillis / (60 * 1000);
        System.out.println();
        double h = total/60.0;
        System.out.println("Hours : "+h);
        return  (u_p+s_p)*h;
    }

    public boolean makePayment(SlotVehicleDTO existing, double cost) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        //update slot vehicle status to 0
        try{
            boolean isUpdated  = slotVehicleDAO.updateStatus(existing.getId());
            if (isUpdated){
                //update slot availability to 1
                boolean isSlotAvailableUpdated =  slotsDAO.updateAvailability(existing.getSlotId(), SlotStatus.Y);
                if (isSlotAvailableUpdated){
                    //add payment
                    Cost cost1 = new Cost(existing.getId(),cost);
                    boolean cost2 = costDAO.createCost(cost1);
                    if (cost2){
                        DBConnection.getInstance().getConnection().commit();
                        return true;
                    }
                }

            }
            connection.rollback();;
            return false;
        }catch (Exception e){
            connection.rollback();
            throw e;
        }finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean parkVehicle(VehicleDTO vehicleByUserId) throws SQLException {
        //find available slot
        List<Slots> allSlotsByType = slotsDAO.getAllAvailableSlotsByType(vehicleByUserId.getVehicleType());
        if (allSlotsByType.size()==0){
            return false;
        }
        Slots slots = allSlotsByType.get(0);
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try{
            //update existing slot details to 0
            boolean isAvailabilityUpdated = slotsDAO.updateAvailability(slots.getId(), SlotStatus.N);
            if (isAvailabilityUpdated){
                //add slot vehicle record with 1
                SlotVehicleDTO slotVehicleDTO = new SlotVehicleDTO(0, vehicleByUserId.getId(), slots.getId(), Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), SlotVehicleStatus.Y);
                boolean slotVehicle = slotVehicleDAO.createSlotVehicle(slotVehicleDTO);
                if (slotVehicle){
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        }catch (Exception e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }
}
