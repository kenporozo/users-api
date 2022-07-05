package com.nobelti.users.service;

import com.nobelti.users.exceptions.InvalidRequestException;
import com.nobelti.users.model.Gender;
import com.nobelti.users.model.Role;
import com.nobelti.users.model.User;
import com.nobelti.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

import static com.nobelti.users.UsersApplication.passwordEncoder;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final StateServiceImpl stateService;
    private final RoleServiceImpl roleService;
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public List<User> getUsers(String gender, Integer age) {
        int i = (gender != null ? 1 : 0) | (age != null ? 2 : 0);
        switch (i) {
            case 0:
                return userRepository.findAll();
            case 1:
                return userRepository.findByGender(Gender.valueOf(gender.toUpperCase()));
            case 2:
                return userRepository.findByAge(age);
            case 3:
                return userRepository.findByGenderAndAge(Gender.valueOf(gender.toUpperCase()), age);
            default:
                return null;
        }
    }

    @Override
    public List<User> getUsersById(List<Long> ids) throws InvalidRequestException {
        List<User> list = new ArrayList<>();
        ids.forEach(id -> {
            if (userRepository.findById(id).isPresent()) {
                list.add(userRepository.findById(id).get());
            }
        });
        if(list.size() == 0){
            message("No se han encontrado usuarios con los ids ingresados");
        }
        return list;
    }

    @Override
    public User insertUser(User user) throws InvalidRequestException {
        //Validaciones
        validateBody(user);
        user.setState(stateService.findByState("ACTIVATED"));
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user) throws InvalidRequestException {
        if (userRepository.findById(user.getIdUser()).isPresent()) {
            //Validaciones
            validateBody(user);
            User userUpdate = userRepository.findById(user.getIdUser()).get();
            userUpdate.setName(user.getName());
            userUpdate.setAge(user.getAge());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setGender(user.getGender());
            userUpdate.setLastName(user.getLastName());
            userRepository.save(userUpdate);
        }else {
            message("El usuario que buscas no existe");
        }
    }

    @Override
    public void deleteUser(Long id) throws InvalidRequestException {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteUserById(id);
        }else {
            message("El usuario que buscas no existe");
        }
    }

    @Override
    public void addRoleToUserById(Long id,String roleName) throws InvalidRequestException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Role role = roleService.findByRole(roleName);
            if(role == null){
                message("El rol ingresado no existe");
            }
            user.get().getRoles().add(role);
        }else {
            message("El usuario que buscas no existe");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userLogin = userRepository.findByEmail(email);
        if(userLogin == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else if(userLogin.getState().getIdState() != 1){
            log.error("User {} inactivated", userLogin.getEmail());
            throw new UsernameNotFoundException("User inactivated");
        }
        else{
            log.info("User found in the database: {}", email);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            userLogin.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            return new org.springframework.security.core.userdetails.User(userLogin.getEmail(), userLogin.getPassword(), authorities);
        }
    }

    private void message(String msgRsp) throws InvalidRequestException{
        throw new InvalidRequestException(msgRsp);
    }

    private void validateBody(User user) throws InvalidRequestException {
        if(user.getName() == null){
            message("Nombre de usuario obligatorio");
        }
        else if(user.getLastName() == null){
            message("Apellido de usuario obligatorio");
        }
        else if(user.getAge() == null || user.getAge() <= 0){
            message("La edad del usuario no es valida");
        }
        else if(!emailPattern.matcher(user.getEmail()).matches()){
            message("Correo no valido");
        }
        else if(userRepository.findByEmail(user.getEmail()) != null){
            message("El correo ya existe");
        }
        else if(user.getPassword() == null){
            message("La password es obligatoria");
        }
    }
}
