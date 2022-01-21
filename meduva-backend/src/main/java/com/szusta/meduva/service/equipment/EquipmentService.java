package com.szusta.meduva.service.equipment;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.exception.HasAssociatedVisitsException;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;
    private ScheduleChecker scheduleChecker;
    private ItemScheduleManager itemScheduleManager;

    @Autowired
    public EquipmentService(EquipmentModelRepository equipmentModelRepository,
                            EquipmentItemRepository equipmentItemRepository,
                            ScheduleChecker scheduleChecker,
                            ItemScheduleManager itemScheduleManager) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.scheduleChecker = scheduleChecker;
        this.itemScheduleManager = itemScheduleManager;
    }

    public List<EquipmentModel> findAllEquipmentModels() {
        return this.equipmentModelRepository.findAll();
    }

    public List<EquipmentModel> findAllUndeletedEquipmentModels() {
        return this.equipmentModelRepository.findAllUndeleted();
    }

    public EquipmentModel findModelById(Long id) {
        return this.equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment model not found with id : " + id));
    }

    public EquipmentItem findItemById(Long id) {
        return this.equipmentItemRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment item not found with id : " + id));
    }

    public boolean doesModelExistByName(String modelName) {
        return this.equipmentModelRepository.existsByName(modelName);
    }

    @Transactional
    public void markModelAsDeleted(Long id) {
        EquipmentModel model = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment model not found with id : " + id));

        markModelItemsAsDeleted(model);
        model.setServices(Collections.emptyList());
        model.deactivate();
        model.markAsDeleted();
        equipmentModelRepository.save(model);
    }

    private void markModelItemsAsDeleted(EquipmentModel model) {
        List<EquipmentItem> itemsToDelete = model.getItems();
        itemsToDelete.forEach(item -> {

            if (scheduleChecker.isUsedInTheFuture(item)) {
                throw new HasAssociatedVisitsException("An item " + item.getName() + " (id = " + item.getId() + ") cannot be deleted because of the future visits");
            }

            item.deactivate();
            item.markAsDeleted();
            equipmentItemRepository.save(item);
        });
    }

    public void deleteAllModels() {
        List<EquipmentModel> modelsToDelete = this.equipmentModelRepository.findAllUndeleted();
        for (EquipmentModel model:modelsToDelete) {
            markModelItemsAsDeleted(model);
            markModelAsDeleted(model.getId());
        }
    }

    public void deleteAllModelsPermanently() {
        this.equipmentItemRepository.deleteAll();
        this.equipmentModelRepository.deleteAll();
    }

    public List<TimeRange> getItemWeeklyUnavailability(EquipmentItem item, TimeRange weekBoundaries) {
        List<EquipmentSchedule> weeklyUnavailability = scheduleChecker.getItemUnavailabilityIn(item, weekBoundaries);
        return weeklyUnavailability.stream()
                .map(unavailableTimeRange ->
                        new TimeRange(unavailableTimeRange.getTimeFrom(), unavailableTimeRange.getTimeTo())
                ).collect(Collectors.toList());
    }

    @Transactional
    public EquipmentSchedule setItemDayUnavailability(Long itemId, Date day) {
        EquipmentItem eqItem = equipmentItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Cannot set item unavailability: item not found in DB with id : " + itemId));

        TimeRange allDay = new TimeRange(
                TimeUtils.getDayStart(day).getTime(),
                TimeUtils.getDayEnd(day).getTime());

        if (!scheduleChecker.isBusyWith(eqItem, EEquipmentStatus.EQUIPMENT_OCCUPIED, allDay)) {
            return itemScheduleManager.setUnavailability(eqItem, allDay);
        } else {
            throw new RuntimeException("Couldn't set item unavailability: item is occupied that day");
        }

    }

    public void deleteDayUnavailability(Long itemId, Date day) {
        this.itemScheduleManager.deleteAllTypeOfEventsBetween(itemId, EEquipmentStatus.EQUIPMENT_UNAVAILABLE, day);
    }
}
