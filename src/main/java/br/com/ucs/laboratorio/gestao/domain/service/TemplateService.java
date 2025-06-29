package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dao.TemplateDao;
import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.TemplateRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TemplateDao templateDao;

    public TemplateResponse create(TemplateDto templateDto) {
        var category = categoryService.findById(templateDto.getCategoryId());
        var templateModel = modelMapper.map(templateDto, TemplateModel.class);
        templateModel.setCategory(category);
        var save = templateRepository.save(templateModel);
        return modelMapper.map(save, TemplateResponse.class);
    }

    public TemplateModel findById(Long id) {
        return templateRepository.findById(id).orElseThrow(() -> new BusinessException("Modelo nao existe"));
    }

    public List<TemplateResponse> findAll() {
        return MapperUtil.mapList(templateRepository.findAll(), TemplateResponse.class);
    }

    public void delete(Long id) {
        templateRepository.delete(findById(id));
    }

    public TemplateResponse update(Long id, TemplateDto templateDto) {
        TemplateModel template = findById(id);
        template.setPeriodMaintenanceType(templateDto.getPeriodMaintenanceType());
        template.setPeriodCalibrationType(templateDto.getPeriodCalibrationType());
        template.setDescription(templateDto.getDescription());
        return MapperUtil.mapObject(templateRepository.save(template), TemplateResponse.class);
    }

    public List<TemplateResponse> filterTemplates(String brand, TemplateType type, Long categoryId) {
        return MapperUtil.mapList(templateDao.filterTemplates(brand, type, categoryId), TemplateResponse.class);
    }
}
