package com.example.service;

import com.example.dao.SlotsDAO;
import com.example.dto.SlotsDTO;
import com.example.entity.custom.Slots;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SlotService {
    private SlotsDAO dao;
    private ModelMapper mapper;
    public SlotService(){
        this.dao = new SlotsDAO();
        this.mapper = new ModelMapper();
    }

    public boolean saveSlot(SlotsDTO dto){
        Slots map = mapper.map(dto, Slots.class);
        return dao.createSlot(map);
    }

    public boolean updateSlot(SlotsDTO dto){
        Slots map = mapper.map(dto, Slots.class);
        return dao.updateSlot(map);
    }

    public boolean delete(int slotId){
        return dao.deleteSlot(slotId);
    }

    public SlotsDTO searchSlotById(int slotId){
        Slots slotById = dao.getSlotById(slotId);
        if (slotById!=null)return mapper.map(slotById,SlotsDTO.class);
        return null;
    }

    public List<SlotsDTO> getAllSlots(){
        return dao.getAllSlots().stream().map(e->{return mapper.map(e,SlotsDTO.class);}).collect(Collectors.toList());
    }

}
