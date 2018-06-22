package com.github.ezh.kinder.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.kinder.model.dto.CLeaveDto;
import com.github.ezh.kinder.model.entity.CLeave;
import org.apache.ibatis.annotations.Param;

import java.util.concurrent.CopyOnWriteArrayList;

public interface CLeaveMapper extends BaseMapper<CLeave> {

    CLeaveDto getById(@Param("id")String id);

    CopyOnWriteArrayList<CLeaveDto> getLeaveList(@Param("obj")CLeave cLeave,@Param("userType")String userType
            ,@Param("offset")Integer offset,@Param("limit")Integer limit);

    Integer readLeave(@Param("id")String id,@Param("userId")String userId);

    Integer audited(CLeave cLeave);

    Integer deleteFlag(@Param("id")String id);
}