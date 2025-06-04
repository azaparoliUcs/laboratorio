package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.configuration.auth.JwtUtil;
import br.com.ucs.laboratorio.gestao.configuration.auth.UserDetailsImpl;
import br.com.ucs.laboratorio.gestao.domain.dto.UserDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.UserResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import br.com.ucs.laboratorio.gestao.infrastructure.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.UserRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtTokenService;

    public String authenticateUser(UserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return jwtTokenService.generateToken(userDetails);
    }

    public UserResponse createUser(UserDto userDto) {
        var userPresent = userRepository.findByEmail(userDto.getEmail()).isPresent();
        if (userPresent){
            throw new BusinessException("Usuario ja existe");
        }
        var passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        var laboratory = laboratoryService.findById(userDto.getLaboratoryId());
        var userModel = modelMapper.map(userDto, UserModel.class);
        userModel.setLaboratory(laboratory);
        userModel.setPassword(encodedPassword);
        UserModel save = userRepository.save(userModel);
        return modelMapper.map(save, UserResponse.class);
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException("Usuario nao existe"));
    }

    public void delete(Long id) {
        UserModel user = findById(id);
        userRepository.delete(user);
    }

    public UserResponse update(Long id, UserDto userDto) {
        UserModel user = findById(id);
        user.setUserType(userDto.getUserType());
        user.setLaboratory(laboratoryService.findById(userDto.getLaboratoryId()));
        user.setName(userDto.getName());
        return MapperUtil.mapObject(userRepository.save(user), UserResponse.class);
    }
}
