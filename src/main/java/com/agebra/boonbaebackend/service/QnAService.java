package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import com.agebra.boonbaebackend.domain.UserRole;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NoSuchUserException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnAService {
    private final QnARepository qnaRepository;
    public void write(QnADto.Request dto, Users user){
        QnA qna = QnA.makeQnA(
          user,dto.getQnaType(),dto.getTitle(),dto.getDescription());
        qnaRepository.save(qna);
    }
    public void update_QnA(Long QnA_pk,QnADto.Request dto, Users user){
        QnA qna = qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));
        if(qna.getUser().getPk() == user.getPk()) {
            qna.editQnA(dto.getQnaType(), dto.getTitle(), dto.getDescription());
        }else{
            throw new NoSuchUserException("다른 유저가 작성한 글입니다 해당 유저 닉네임:"+qna.getUser().getNickname());
        }
    }

    public void delete(Long QnA_pk){
        QnA qna = qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));
        qnaRepository.delete(qna);
    }


    @Transactional(readOnly = true)
    public QnADto.Response_oneQnA one_QnA(Long QnA_pk){
        QnA qna =qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다."));
        String status;
        int isReply;
        if(qna.getReplyText()==null) {
            status = "waiting";
            isReply=0;
        } else {
            status = "answered";
            isReply=1;
        }
        QnADto.Response_oneQnA dto = QnADto.Response_oneQnA.builder()
          .qnaType(qna.getQnaType())
          .status(status)
          .isReply(isReply)
          .replyText(qna.getReplyText())
          .title(qna.getTitle())
          .userName(qna.getUserName())
          .createAt(qna.getCreateAt())
          .description(qna.getDescriptions())
          .build();
        return dto;
    }
    @Transactional(readOnly = true)
    public List<QnADto.Response_AllQnA> all_QnA(Pageable pageable, QnAType category){
        List<QnA> qnaList = qnaRepository.findByQnaType(category);
        List<QnADto.Response_AllQnA> dtoList = new ArrayList<>();
        String status;
        for(QnA qna : qnaList){
            if(qna.getReplyText()==null) {
                status = "waiting";
            } else {
                status = "answered";
            }
            QnADto.Response_AllQnA dto= QnADto.Response_AllQnA.builder()
              .qnaPk(qna.getPk())
              .qnaType(qna.getQnaType())
              .status(status)
              .title(qna.getTitle())
              .userName(qna.getUserName())
              .createAt(qna.getCreateAt())
              .build();
            dtoList.add(dto);
        }
        pageable= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"createAt"));
        int start = (int) pageable.getOffset();
        int end=Math.min((start + pageable.getPageSize()),dtoList.size());
        Page<QnADto.Response_AllQnA> qnaAll= new PageImpl<>(dtoList.subList(start,end),pageable,dtoList.size());
        List<QnADto.Response_AllQnA> qnaPage = qnaAll.getContent();
        return qnaPage;
    }


    public void reply_QnA(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .makeReply(reply);
    }
    public void update_Reply(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .editReply(reply);
    }
    public void delete_Reply(Long QnA_pk){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .deleteReply();
    }
}