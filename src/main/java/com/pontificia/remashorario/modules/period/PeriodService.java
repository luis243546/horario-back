//package com.pontificia.remashorario.modules.period;
//
//import com.pontificia.remashorario.modules.period.dto.PeriodRequestDTO;
//import com.pontificia.remashorario.modules.period.dto.PeriodResponseDTO;
//import com.pontificia.remashorario.modules.period.mapper.PeriodMapper;
//import com.pontificia.remashorario.utils.abstractBase.BaseService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class PeriodService extends BaseService<PeriodEntity> {
//
//    private final PeriodRepository periodRepository;
//    private final PeriodMapper periodMapper;
//
//    public PeriodService(PeriodRepository periodRepository, PeriodMapper periodMapper) {
//        super(periodRepository);
//        this.periodRepository = periodRepository;
//        this.periodMapper = periodMapper;
//    }
//
//    public List<PeriodResponseDTO> getAllPeriods() {
//        return periodMapper.toResponseDTOList(findAll());
//    }
//
//    public PeriodResponseDTO getPeriodById(UUID uuid) {
//        return periodMapper.toResponseDTO(findPeriodOrThrow(uuid));
//    }
//
//    @Transactional
//    public PeriodResponseDTO createPeriod(PeriodRequestDTO dto) {
//        if (periodRepository.existsByName(dto.getName())) {
//            throw new IllegalArgumentException("Ya existe un periodo con ese nombre");
//        }
//        PeriodEntity entity = periodMapper.toEntity(dto);
//        return periodMapper.toResponseDTO(save(entity));
//    }
//
//    @Transactional
//    public PeriodResponseDTO updatePeriod(UUID uuid, PeriodRequestDTO dto) {
//        PeriodEntity period = findPeriodOrThrow(uuid);
//        if (!period.getName().equals(dto.getName()) && periodRepository.existsByName(dto.getName())) {
//            throw new IllegalArgumentException("Ya existe otro periodo con ese nombre");
//        }
//        periodMapper.updateEntityFromDTO(period, dto);
//        return periodMapper.toResponseDTO(save(period));
//    }
//
//    @Transactional
//    public void deletePeriod(UUID uuid) {
//        PeriodEntity period = findPeriodOrThrow(uuid);
//        deleteById(period.getUuid());
//    }
//
//    public PeriodEntity findPeriodOrThrow(UUID uuid) {
//        return findById(uuid).orElseThrow(() -> new EntityNotFoundException("Periodo no encontrado con ID: " + uuid));
//    }
//}


package com.pontificia.remashorario.modules.period;

import com.pontificia.remashorario.modules.period.dto.PeriodRequestDTO;
import com.pontificia.remashorario.modules.period.dto.PeriodResponseDTO;
import com.pontificia.remashorario.modules.period.mapper.PeriodMapper;
import com.pontificia.remashorario.utils.abstractBase.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PeriodService extends BaseService<PeriodEntity> {

    private final PeriodRepository periodRepository;
    private final PeriodMapper periodMapper;

    public PeriodService(PeriodRepository periodRepository, PeriodMapper periodMapper) {
        super(periodRepository);
        this.periodRepository = periodRepository;
        this.periodMapper = periodMapper;
    }

    public List<PeriodResponseDTO> getAllPeriods() {
        return periodMapper.toResponseDTOList(findAll());
    }

    public PeriodResponseDTO getPeriodById(UUID uuid) {
        return periodMapper.toResponseDTO(findPeriodOrThrow(uuid));
    }

    // ✅ NUEVO: Obtener período activo basado en la fecha actual
    // Este método es esencial para que los docentes puedan ver sus clases automáticamente
    // sin necesidad de seleccionar manualmente un período académico
    public PeriodResponseDTO getActivePeriod() {
        LocalDate today = LocalDate.now();
        PeriodEntity activePeriod = periodRepository.findByDate(today)
                .orElseThrow(() -> new EntityNotFoundException("No hay un período activo en este momento"));
        return periodMapper.toResponseDTO(activePeriod);
    }

    @Transactional
    public PeriodResponseDTO createPeriod(PeriodRequestDTO dto) {
        if (periodRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un periodo con ese nombre");
        }
        PeriodEntity entity = periodMapper.toEntity(dto);
        return periodMapper.toResponseDTO(save(entity));
    }

    @Transactional
    public PeriodResponseDTO updatePeriod(UUID uuid, PeriodRequestDTO dto) {
        PeriodEntity period = findPeriodOrThrow(uuid);
        if (!period.getName().equals(dto.getName()) && periodRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Ya existe otro periodo con ese nombre");
        }
        periodMapper.updateEntityFromDTO(period, dto);
        return periodMapper.toResponseDTO(save(period));
    }

    @Transactional
    public void deletePeriod(UUID uuid) {
        PeriodEntity period = findPeriodOrThrow(uuid);
        deleteById(period.getUuid());
    }

    public PeriodEntity findPeriodOrThrow(UUID uuid) {
        return findById(uuid).orElseThrow(() -> new EntityNotFoundException("Periodo no encontrado con ID: " + uuid));
    }
}