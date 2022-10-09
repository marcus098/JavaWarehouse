package com.example.warehouse.service;

import com.example.warehouse.model.Position;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.PositionRestRepository;
import com.example.warehouse.repository.ProductRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionRestService {
    @Autowired
    private PositionRestRepository positionRestRepository;

    public List<Position> getPositions(){
        return positionRestRepository.getPositions();
    }

    public List<Position> searchPositionByName(String name, long id){
        List<Position> list = new ArrayList<>();
        list.addAll(positionRestRepository.searchPositionByName(name));
        list.addAll(positionRestRepository.searchPositionByName(name,id));
        return list;
    }

    public List<Position> searchPositionByIdProduct(long idProduct) {
        return positionRestRepository.searchPositionByIdProduct(idProduct);
    }

    public ReturnWithMessage addPosition(Position position){
        if(positionRestRepository.addPosition(position))
            return new ReturnWithMessage(true, "Posizione aggiunta");
        return new ReturnWithMessage(false, "Posizione non aggiunta");
    }

    public ReturnWithMessage updateProductPosition(long idPosition, long idProduct){
        if(positionRestRepository.updateProductPosition(idPosition, idProduct))
            return new ReturnWithMessage(true, "Modificato con successo");
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage updateProductPositionNull(long idPosition){
        if(positionRestRepository.updateProductPosition(idPosition))
            return new ReturnWithMessage(true, "Modificato con successo");
        return new ReturnWithMessage(false, "Errore");
    }

    public boolean emptyPosition(long id){
        return positionRestRepository.emptyPosition(id);
    }

    public boolean deletePosition(long id){
        return positionRestRepository.deletePosition(id);
    }
}
