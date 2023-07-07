package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Tip;
import com.agebra.boonbaebackend.dto.TipDto;
import com.agebra.boonbaebackend.repository.TipRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TipServiceTest {
    @Autowired
    private TipService tipService;
    @Autowired
    private TipRepository tipRepository;
    private String firstTip="1번팁";
    private String EditTip="test2";
    @BeforeEach
    void setup(){

    }
    @Test
    @DisplayName("팁 생성 확인")
    @Transactional
    @Order(1)
    void writeTip(){
        tipService.addTip(firstTip);
        String Text = tipService.getTip();
        Assertions.assertEquals(firstTip,Text);

    }
    @Test
    @DisplayName("팁 수정 확인")
    @Transactional
    @Order(2)
    void updateTip(){
        Tip tips = Tip.builder().content(firstTip).build();
        tipRepository.save(tips);
        tipService.modifyTip(tips.getPk(),EditTip);
        Assertions.assertEquals(EditTip,tips.getContent());
    }

    @Test
    @DisplayName("팁 전체 확인")
    @Transactional
    @Order(3)
    void allTip(){
        Tip tips = Tip.builder().content(firstTip).build();
        Tip tips2 = Tip.builder().content(EditTip).build();
        tipRepository.save(tips);
        tipRepository.save(tips2);
        List<TipDto.Tip_List> dto = tipService.getAll();
        List<String> StringList=new ArrayList<>();
        for(TipDto.Tip_List result:dto){
            StringList.add(result.getName());
        }
    Assertions.assertEquals(List.of(tips.getContent(),tips2.getContent()),StringList);
    }
}
