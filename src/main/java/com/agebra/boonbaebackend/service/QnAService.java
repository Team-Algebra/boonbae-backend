package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NoSuchUserException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.QnARepository;
import com.agebra.boonbaebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnAService {
    private final QnARepository qnaRepository;
    private final UserRepository userRepository;
    public QnA write(QnADto.Request dto, Users user){
        QnA qna = QnA.makeQnA(
                user,dto.getQnaType(),dto.getTitle(),dto.getDescription());
        QnA save = qnaRepository.save(qna);
        return save;
    }

    public void delete(Long QnA_pk){
        QnA qna = qnaRepository.findById(QnA_pk)
                .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));
        qnaRepository.delete(qna);
    }
    public void reply_QnA(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
                .orElseThrow(() -> new ForbiddenException("일치하는 QnA가 없습니다"))
                .makeReply(reply);
    }
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
                .create_date(qna.getCreateAt())
                .description(qna.getDescriptions())
                .build();
        return dto;
    }
    public List<QnADto.Response_AllQnA> all_QnA(Pageable pageable, QnAType qnAType){
        List<QnA> qnaList;

        if (qnAType == null)
            qnaList = qnaRepository.findAll();
        else
            qnaList = qnaRepository.findByQnaType(qnAType);

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
                    .create_date(qna.getCreateAt())
                    .build();
            dtoList.add(dto);
        }
        pageable= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = (int) pageable.getOffset();
        int end=Math.min((start + pageable.getPageSize()),dtoList.size());
        Page<QnADto.Response_AllQnA> qnaAll= new PageImpl<>(dtoList.subList(start,end),pageable,dtoList.size());
        List<QnADto.Response_AllQnA> qnaPage = qnaAll.getContent();
        return qnaPage;
    }
    public void update_QnA(Long QnA_pk,QnADto.Request dto, Users user){
//        qnaRepository.findById(QnA_pk)
//                .orElseThrow(() -> new ForbiddenException("일치하는 QnA가 없습니다"))
//                .editQnA(user,dto.getQnaType(),dto.getTitle(),dto.getDescription());

        QnA findQna = qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));//404

        Users qnaUser = findQna.getUser();

        if (user.getPk() == qnaUser.getPk()) {
            findQna.editQnA(dto.getQnaType(), dto.getTitle(), dto.getDescription());
        } else {
            throw new ForbiddenException("권한이 없는 사용자입니다");
        }
    }
    public void update_Reply(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
                .orElseThrow(() -> new ForbiddenException("일치하는 QnA가 없습니다"))
                .editReply(reply);
    }
}
