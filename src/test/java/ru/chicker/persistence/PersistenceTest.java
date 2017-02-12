package ru.chicker.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;
import ru.chicker.entities.Person;
import ru.chicker.repositories.PersonRepository;
import ru.chicker.utils.CollectionUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestAppConfig.class,
    PersistenceConfig.class,
    TestDataSourceConfig.class})
@WebAppConfiguration
public class PersistenceTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void testSave() throws Exception {
        Person person1 = new Person("Katy", "Jhonson");
        Person person2 = new Person("Elena", "Markova");

        personRepository.save(person1);
        personRepository.save(person2);

        // The size should be 4, because the table "Persons" already contains 2 
        // persons initially
        assertThat(CollectionUtils.sizeOfIterable(personRepository.findAll())
            , is(4));
    }
}
