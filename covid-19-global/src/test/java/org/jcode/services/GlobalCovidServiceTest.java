package org.jcode.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalCovidServiceTest {

    @Autowired
    GlobalCovidServiceImpl globalCovidService;


    @Test
    public void getDateKeysHasValidStartingAndEndingDates(){
        List<String> dateKeys = GlobalCovidServiceImpl.getDateKeys();

        assertEquals(dateKeys.get(0), "1/22/20");
        assertEquals(dateKeys.get(dateKeys.size()-1), "6/27/21");
    }
}


