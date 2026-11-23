package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.animal;

import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.PetDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceAdapterTest {

    @Mock
    private PetDao petDao;
    @Mock
    private UserDao userDao;
    @Mock
    private VaccineDao vaccineDao;

    @InjectMocks
    private PetServiceAdapter petService;


    @Test
    public void shouldCreatePetWhenDataIsValid() {
        PetModel pet = mock(PetModel.class);
        UserModel owner = mock(UserModel.class);
        List<PetModel> ownerPets = new ArrayList<>();
        when(pet.getName()).thenReturn("Rex");
        when(pet.getOwnerId()).thenReturn(1);
        when(userDao.readyById(1)).thenReturn(owner);
        when(owner.getPets()).thenReturn(ownerPets);
        when(petDao.add(pet)).thenReturn(5);

        int result = petService.create(pet);

        assertEquals(5, result);
        assertTrue(ownerPets.contains(pet));
        verify(petDao).add(pet);
    }

    @Test
    public void shouldNotCreateWhenPetIsNull() {
        assertEquals(0, petService.create(null));
        verifyNoInteractions(petDao, userDao);
    }

    @Test
    public void shouldNotCreateWhenOwnerDoesNotExist() {
        PetModel pet = mock(PetModel.class);
        when(pet.getName()).thenReturn("Rex");
        when(pet.getOwnerId()).thenReturn(99);

        assertEquals(0, petService.create(pet));
        verify(petDao, never()).add(pet);
    }
}