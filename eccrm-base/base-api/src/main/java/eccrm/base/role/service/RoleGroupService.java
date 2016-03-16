package eccrm.base.role.service;

import eccrm.base.role.vo.RoleGroupVo;

import java.util.List;

/**
 * Generated by miles on 2014-07-21.
 */

public interface RoleGroupService {

    /**
     * 根据用户组id保存一组角色的关联关系
     *
     * @param groupId 组id
     * @param roleIds 角色id
     */
    void save(String groupId, String[] roleIds);

    /**
     * 根据用户组id查询所有的角色
     *
     * @param userGroupId 用户组id
     * @return 角色集合
     */
    List<RoleGroupVo> queryByGroup(String userGroupId);

    /**
     * 根据用户组id数组查询所有的角色集合（去重）
     *
     * @param userGroupIds 用户组id数组
     * @return 角色集合
     */
    List<RoleGroupVo> queryByGroups(String[] userGroupIds);

    /**
     * 删除某个用户组的所有角色
     *
     * @param groupId 用户组id
     */
    void deleteByGroupId(String groupId);

    void deleteByIds(String[] ids);
}
