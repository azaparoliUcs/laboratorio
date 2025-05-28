package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.CategoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.CategoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.CategoryRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryModel findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria nao existe"));
    }

    public CategoryResponse create(CategoryDto categoryDto){
        var save = categoryRepository.save(CategoryModel.builder()
                .name(categoryDto.getName())
                .build());
        return modelMapper.map(save, CategoryResponse.class);
    }

    public List<CategoryResponse> findAll() {
        return MapperUtil.mapList(categoryRepository.findAll(), CategoryResponse.class);
    }
}
