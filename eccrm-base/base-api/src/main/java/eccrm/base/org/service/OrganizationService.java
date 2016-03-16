package eccrm.base.org.service;


import com.ycrl.core.pager.PageVo;
import eccrm.base.org.bo.OrganizationBo;
import eccrm.base.org.domain.Organization;
import eccrm.base.org.vo.OrganizationVo;

import java.util.List;
import java.util.Set;

/**
 * Generated by yanhx on 2014-10-13.
 */

public interface OrganizationService {

    String save(Organization organization);

    void update(Organization organization);

    PageVo query(OrganizationBo bo);

    PageVo queryAll(OrganizationBo bo);

    OrganizationVo findById(String id);

    List<OrganizationVo> tree(OrganizationBo bo);

    List<OrganizationVo> trees();

    PageVo queryAllChildren(String id);

    /**
     * 查询当前节点的直接子节点
     * 只返回id、name、parentId和isParent属性
     *
     * @param id 当前节点的id，如果当前id为空，则表示查询第一级
     * @return 父节点
     */
    List<OrganizationVo> queryChildren(String id);

    /**
     * 查询当前节点的直接子节点（仅查询状态为有效的数据）
     * 只返回id、name、parentId和isParent属性
     *
     * @param id 当前节点的id，如果当前id为空，则表示查询第一级
     * @return 父节点
     */
    List<OrganizationVo> queryValidChildren(String id);

    void deleteByIds(String... ids);

    boolean addPostion(String id) throws Exception;


    /**
     * 查询指定组织机构下的所有子组织机构的id集合（包括隔代）
     *
     * @param orgs 组织机构id集合
     * @return 子组织机构id集合
     */
    List<String> findAllChildrenIds(Set<String> orgs);

    /**
     * 带权限的高级分页查询
     * <p>该方法会通过数据权限进行数据过滤</p>
     */
    PageVo permissionPageQuery(OrganizationBo bo);

    /**
     * 带数据权限的高级查询
     * <p>该方法会通过数据权限进行数据过滤</p>
     */
    List<OrganizationVo> permissionQuery(OrganizationBo bo);

    /**
     * 带权限的数据查询
     * 只查询有权限的组织机构的根
     */
    List<OrganizationVo> permissionRootQuery();

    /**
     * 查询指定用户被授权的所有的系统的集合
     * 只返回系统编号（busiTypeId）和系统名称（busiTypeName）
     */
    List<OrganizationVo> permissionPersonalParams();

}