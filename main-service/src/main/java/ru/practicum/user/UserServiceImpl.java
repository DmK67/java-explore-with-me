package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static ru.practicum.user.dto.UserMapper.toUser;
import static ru.practicum.user.dto.UserMapper.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.info("Getting a list of users by IDs: ids = " + ids + ", from = " + from + ", size = " + size);
        return toUserDto(userRepository.findByIdIn(ids, PageRequest.of(from / size, size)));
    }

    public List<UserDto> getUsers(int from, int size) {
        log.info("Getting a list of all users: from = " + from + ", size = " + size);
        return toUserDto(userRepository.findAll(PageRequest.of(from / size, size)));
    }

    public UserDto createUser(NewUserRequestDto newUserRequestDto) {
        log.info("Creating a new user: " + newUserRequestDto);
        return toUserDto(userRepository.save(toUser(newUserRequestDto)));
    }

    public void deleteUser(Long id) {
        log.info("Deleting a user with an ID = " + id);
        userRepository.deleteById(id);
    }
}
