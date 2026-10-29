package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.user;

import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.EnterpriseModel;
import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.EnterpriseDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.PersonDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.adoption.AdoptionService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.PetService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.vaccine.VaccineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceAdapterTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PersonDao personDao;
    @Mock
    private EnterpriseDao enterpriseDao;
    @Mock
    private PetService petService;
    @Mock
    private FarmAnimalService farmAnimalService;
    @Mock
    private VaccineService vaccineService;
    @Mock
    private AdoptionService adoptionService;
    @Mock
    private AnnouncementDao announcementDao;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceAdapter userService;


    @Test
    public void shouldRejectEmptyPassword() {
        assertTrue(userService.isPasswordInvalid(""));
    }

    @Test
    public void shouldRejectTooShortPassword() {
        assertTrue(userService.isPasswordInvalid("a"));
    }

    @Test
    public void shouldRejectNegativeId() {
        assertTrue(userService.isIdInvalid(-1));
    }

    @Test
    public void shouldCreatePersonWhenDataIsValid() {
        PersonModel person = mock(PersonModel.class);
        when(person.getPassword()).thenReturn("1234");
        when(person.getEmail()).thenReturn("maria@email.com");
        when(person.getFullname()).thenReturn("Maria Silva");
        when(personDao.add(person)).thenReturn(1);

        int result = userService.create(person);

        assertEquals(1, result);
        verify(personDao).add(person);
    }

    @Test
    public void shouldNotCreateUserWhenEmailHasNoAtSign() {
        PersonModel person = mock(PersonModel.class);
        when(person.getPassword()).thenReturn("1234");
        when(person.getEmail()).thenReturn("mariaemail.com");
        when(person.getFullname()).thenReturn("Maria Silva");

        int result = userService.create(person);

        assertEquals(0, result);
        verifyNoInteractions(personDao, enterpriseDao);
    }

    @Test
    public void shouldNotCreateEnterpriseWithoutDocument() {
        EnterpriseModel enterprise = mock(EnterpriseModel.class);
        when(enterprise.getPassword()).thenReturn("1234");
        when(enterprise.getEmail()).thenReturn("contato@ong.com");
        when(enterprise.getFullname()).thenReturn("ONG Amigo Fiel");
        when(enterprise.getAccountType()).thenReturn(AccountType.ENTERPRISE);

        int result = userService.create(enterprise);

        assertEquals(0, result);
        verifyNoInteractions(personDao, enterpriseDao);
    }


    @Test
    public void shouldFindUserByEmail() {
        UserModel user = mock(UserModel.class);
        when(userDao.readByEmail("maria@email.com")).thenReturn(user);

        UserModel result = userService.findByEmail("maria@email.com");

        assertSame(user, result);
    }

    @Test
    public void shouldReturnNullWhenEmailIsEmpty() {
        assertNull(userService.findByEmail(""));
        verifyNoInteractions(userDao);
    }

    @Test
    public void shouldReturnNullWhenEmailHasNoAtSign() {
        assertNull(userService.findByEmail("mariaemail.com"));
        verifyNoInteractions(userDao);
    }


    @Test
    public void shouldFindUserByIdAndFillAnimals() {
        UserModel user = mock(UserModel.class);
        when(userDao.readyById(1)).thenReturn(user);

        UserModel result = userService.findById(1);

        assertSame(user, result);
        verify(user).setPets(anyList());
        verify(user).setFarmAnimalModels(anyList());
    }

    @Test
    public void shouldReturnNullWhenIdIsNegative() {
        assertNull(userService.findById(-1));
        verifyNoInteractions(userDao);
    }

    @Test
    public void shouldReturnNullWhenUserDoesNotExist() {
        assertNull(userService.findById(99));
        verify(userDao).readyById(99);
    }


    @Test
    public void shouldReturnOnlyPetsFromOwner() {
        UserModel owner = mock(UserModel.class);
        PetModel ownPet = mock(PetModel.class);
        PetModel otherPet = mock(PetModel.class);
        when(userDao.readyById(1)).thenReturn(owner);
        when(petService.findAll()).thenReturn(List.of(ownPet, otherPet));
        when(ownPet.getOwnerId()).thenReturn(1);
        when(otherPet.getOwnerId()).thenReturn(2);

        List<PetModel> result = userService.showAllPetsByOwnerId(1);

        assertEquals(1, result.size());
        assertSame(ownPet, result.get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenOwnerIdIsNegative() {
        assertTrue(userService.showAllPetsByOwnerId(-1).isEmpty());
        verifyNoInteractions(userDao, petService);
    }

    @Test
    public void shouldReturnEmptyListWhenOwnerDoesNotExist() {
        assertTrue(userService.showAllPetsByOwnerId(99).isEmpty());
        verifyNoInteractions(petService);
    }


    @Test
    public void shouldFindPetWhenItBelongsToOwner() {
        PetModel pet = mock(PetModel.class);
        when(petService.findById(10)).thenReturn(pet);
        when(pet.getOwnerId()).thenReturn(1);

        PetModel result = userService.findPetByOwnerId(1, 10);

        assertSame(pet, result);
    }

    @Test
    public void shouldReturnNullWhenPetIdIsNegative() {
        assertNull(userService.findPetByOwnerId(1, -1));
        verifyNoInteractions(petService);
    }

    @Test
    public void shouldReturnNullWhenPetBelongsToAnotherOwner() {
        PetModel pet = mock(PetModel.class);
        when(petService.findById(10)).thenReturn(pet);
        when(pet.getOwnerId()).thenReturn(2);

        assertNull(userService.findPetByOwnerId(1, 10));
    }


    @Test
    public void shouldUpdatePasswordWhenOldPasswordMatches() {
        UserModel user = mock(UserModel.class);
        when(userDao.readyById(1)).thenReturn(user);
        when(user.getPassword()).thenReturn("hash");
        when(passwordEncoder.matches("1234", "hash")).thenReturn(true);
        when(userDao.updatePassword(1, "5678")).thenReturn(true);

        assertTrue(userService.updatePassword(1, "1234", "5678"));
    }

    @Test
    public void shouldNotUpdatePasswordWhenNewPasswordIsTooShort() {
        assertFalse(userService.updatePassword(1, "1234", "a"));
        verifyNoInteractions(userDao);
    }

    @Test
    public void shouldNotUpdatePasswordWhenOldPasswordDoesNotMatch() {
        UserModel user = mock(UserModel.class);
        when(userDao.readyById(1)).thenReturn(user);
        when(user.getPassword()).thenReturn("hash");
        when(passwordEncoder.matches("senhaerrada", "hash")).thenReturn(false);

        assertFalse(userService.updatePassword(1, "senhaerrada", "5678"));
        verify(userDao, never()).updatePassword(1, "5678");
    }

    @Test
    public void shouldUpdatePersonInformation() {
        PersonModel existing = mock(PersonModel.class);
        PersonModel changes = mock(PersonModel.class);
        when(userDao.readyById(1)).thenReturn(existing);
        when(changes.getFullname()).thenReturn("Maria Souza");

        boolean result = userService.update(1, changes);

        assertTrue(result);
        verify(existing).setFullname("Maria Souza");
        verify(personDao).updateInformation(1, existing);
    }

    @Test
    public void shouldNotUpdateWhenUserDoesNotExist() {
        PersonModel changes = mock(PersonModel.class);

        assertFalse(userService.update(-1, changes));
        verifyNoInteractions(personDao, enterpriseDao);
    }

    @Test
    public void shouldNotUpdateWhenPhoneIsInvalid() {
        PersonModel existing = mock(PersonModel.class);
        PersonModel changes = mock(PersonModel.class);
        when(userDao.readyById(1)).thenReturn(existing);
        when(changes.getPhone()).thenReturn("123");

        assertFalse(userService.update(1, changes));
        verifyNoInteractions(personDao, enterpriseDao);
    }

    @Test
    public void shouldDeleteUserWithPetsAndVaccines() {
        UserModel user = mock(UserModel.class);
        PetModel pet = mock(PetModel.class);
        VaccineModel vaccine = mock(VaccineModel.class);
        when(userDao.readyById(1)).thenReturn(user);
        when(petService.findAll()).thenReturn(List.of(pet));
        when(pet.getOwnerId()).thenReturn(1);
        when(pet.getId()).thenReturn(10);
        when(petService.showAllVaccineByAnimalId(10)).thenReturn(List.of(vaccine));
        when(vaccine.getId()).thenReturn(5);

        userService.delete(1);

        verify(vaccineService).delete(5);
        verify(petService).delete(10);
        verify(userDao).remove(1);
    }

    @Test
    public void shouldNotDeleteWhenIdIsNegative() {
        userService.delete(-1);

        verifyNoInteractions(userDao);
    }

    @Test
    public void shouldNotDeleteWhenUserDoesNotExist() {
        userService.delete(7);

        verify(userDao).readyById(7);
        verify(userDao, never()).remove(7);
    }
}