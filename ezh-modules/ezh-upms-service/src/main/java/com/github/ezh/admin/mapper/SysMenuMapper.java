package com.github.ezh.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.ezh.admin.model.entity.SysMenu;
import com.github.ezh.common.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author solor
 * @since 2017-10-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过角色名查询菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    Set<MenuVo> findMenuByRoleName(@Param("role" ) String role);
}