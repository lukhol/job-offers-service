package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.UserDto;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;
    private List<User> users = new ArrayList<>();

    @Before
    public void setUp() {
        userService = new UserServiceImpl(passwordEncoder, userRepository);
        IntStream.range(0, 5)
                .forEach(i -> {
                    User user = new User("lukhol" + i, "password");
                    users.add(user);
                });
    }

    @Test
    public void canFindAll_withHiddenPassword() {
        when(userRepository.findAllPersisted()).thenReturn(users);

        var allUsers = userService.findAll();
        assertThat(allUsers).hasSize(5);
        assertThat(allUsers).containsAll(users);
        for (User user : allUsers) {
            assertThat(user.getPassword()).isNotEqualTo("password");
        }
    }

    @Test
    public void canFindById_withHiddenPassword() {
        User expected = new User("login", "password");
        expected.setId(1L);
        when(userRepository.findPersistedById(1L)).thenReturn(Optional.of(expected));

        var actual = userService.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getPassword()).isNotEqualTo("password");
    }

    @Test(expected = ServiceValidationException.class)
    public void cannotCreateUser_emptyLogin() {
        var userDto = UserDto.builder().login("").password("password").build();
        userService.create(userDto);
    }

    @Test(expected = ServiceValidationException.class)
    public void cannotCreateUser_emptyPassword() {
        var userDto = UserDto.builder().login("123").password(null).build();
        userService.create(userDto);
    }

    @Test
    public void cannotCreateUser_notUniqueLogin() {
        expectedEx.expect(ServiceValidationException.class);
        expectedEx.expectMessage("This login is already occupied. Choose another one.");

        when(userRepository.isLoginUnique("login")).thenReturn(false);

        var userDto = UserDto.builder().login("login").password("password").build();
        userService.create(userDto);
    }

    @Test
    public void canCreateUser_success() {
        var userDto = UserDto.builder().login("login").password("password").build();
        when(userRepository.save(any(User.class))).thenReturn(new User("", "password"));
        when(userRepository.isLoginUnique(anyString())).thenReturn(true);

        var actual = userService.create(userDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getPassword()).isEqualTo("password");
        assertThat(actual.getLogin()).isEqualTo("login");
    }

    @Test
    public void cannotUpdate_notFoundUser() throws Exception {
        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage("Not found user with id: 1");
        userService.update(1L, UserDto.builder().login("login").password("password").build());
    }

    @Test
    public void cannotUpdate_notValidDto() throws Exception {
        expectedEx.expect(ServiceValidationException.class);
        expectedEx.expectMessage("Username and password cannot be empty.");
        userService.update(1L, new UserDto());
    }

    @Test
    public void canUpdate_success() throws Exception {
        User user = User.builder().login("login").password("password").build();
        UserDto userDto = UserDto.builder().login("newLogin").password("newPassword").build();
        when(userRepository.findPersistedById(1L)).thenReturn(Optional.of(user));

        var actual = userService.update(1L, userDto);

        assertThat(actual).isNotNull();
        assertThat(actual.getLogin()).isEqualTo("newLogin");
        assertThat(actual.getPassword()).isNotEqualTo("password");
    }

    @Test
    public void cannotRemove_notFoundUser() throws Exception {
        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage("Not found user with id: 1");

        when(userRepository.findPersistedById(1L)).thenReturn(Optional.empty());

        userService.removeUserById(1L, null);
    }

    @Test
    public void cannotRemove_usersNotMatch() throws Exception {
        expectedEx.expect(ServiceValidationException.class);
        expectedEx.expectMessage("Are you trying to remove another user than you? This is not allowed.");

        when(userRepository.findPersistedById(1L)).thenReturn(Optional.of(new User(1L, "login", "password")));

        userService.removeUserById(1L, new User(2L, null, null));
    }

    @Test
    public void canRemoveUser_success() throws Exception {
        User user = new User(1L, "login", "password");
        when(userRepository.findPersistedById(1L)).thenReturn(Optional.of(user));

        userService.removeUserById(1L, user);

        verify(userRepository, times(1)).findPersistedById(1L);
        verify(userRepository, times(1)).softDelete(user);
        verifyNoMoreInteractions(userRepository);
    }
}