package com.szusta.meduva.model;

import com.szusta.meduva.model.common.Activable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentModel extends Activable {

    private String name;

    @OneToMany(mappedBy = "equipmentModel")
    private List<EquipmentItem> items;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "service_equipment_model",
            joinColumns = @JoinColumn(name = "equipment_model_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    public EquipmentModel(String name, boolean isActive, boolean deleted) {
        this.name = name;
        this.isActive = isActive;
        this.deleted = deleted;
    }
}
