package eccrm.base.employee.dao.impl;

import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.exception.Argument;
import com.ycrl.core.hibernate.filter.FilterFieldType;
import eccrm.base.employee.bo.EmployeeBo;
import eccrm.base.employee.dao.EmployeeDao;
import eccrm.base.employee.domain.Employee;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Generated by yanhx on 2014-10-13.
 */

@Repository("employeesDao")
public class EmployeeDaoImpl extends HibernateDaoHelper implements EmployeeDao {
    public static final String FILTER_NAME = "QUERY_EMPLOYEE";

    @Override
    public String save(Employee employee) {
        return (String) getSession().save(employee);
    }

    @Override
    public void update(Employee employee) {
        getSession().update(employee);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Employee> query(EmployeeBo bo) {
        if (bo == null) {
            bo = new EmployeeBo();
        }
        Criteria criteria = getDefaultCriteria(bo);
        return criteria.list();
    }

    @Override
    public long getTotal(EmployeeBo bo) {
        Criteria criteria = null;
        if (bo.getPermission() != null && bo.getPermission()) {
            criteria = createRowCountsCriteria(Employee.class, FILTER_NAME, "orgId", FilterFieldType.ORG);
        } else {
            criteria = createRowCountsCriteria(Employee.class);
        }
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public int deleteById(String id) {
        return getSession().createQuery("delete from " + Employee.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public Employee findById(String id) {
        return (Employee) getSession().get(Employee.class, id);
    }

    @Override
    public Employee findByCode(String id) {
        String sql = "from " + Employee.class.getName() + " e where e.employeeCode='" + id + "'";

        return (Employee) getSession().createQuery(sql).uniqueResult();
    }

    /**
     * 获得默认的org.hibernate.Criteria对象,并根据bo进行初始化（如果bo为null，则会新建一个空对象）
     * 为了防止新的对象中有数据，建议实体/BO均采用封装类型
     */
    private Criteria getDefaultCriteria(EmployeeBo bo) {
        Criteria criteria = null;
        if (bo.getPermission() != null && bo.getPermission()) {
            criteria = createCriteria(Employee.class, FILTER_NAME, "orgId", FilterFieldType.ORG);
        } else {
            criteria = createCriteria(Employee.class);
        }
        initCriteria(criteria, bo);
        return criteria;
    }

    /**
     * 根据BO初始化org.hibernate.Criteria对象
     * 如果org.hibernate.Criteria为null，则抛出异常
     * 如果BO为null，则新建一个空的对象
     */
    private void initCriteria(Criteria criteria, EmployeeBo bo) {
        if (criteria == null) {
            throw new IllegalArgumentException("criteria must not be null!");
        }
        if (bo == null) bo = new EmployeeBo();
        if (bo.getValid() != null && bo.getValid()) {
            // 状态为正式、调动中、实习
            criteria.add(Restrictions.in("status", new String[]{"2", "9", "11"}));
        }
        criteria.add(Example.create(bo).enableLike(MatchMode.START).ignoreCase());
    }

    @Override
    public String findNameById(String employeeId) {
        Argument.isEmpty(employeeId, "根据ID查找员工姓名时,员工ID不能为空!");
        return (String) createCriteria(Employee.class)
                .setProjection(Projections.property("employeeName"))
                .add(Restrictions.idEq(employeeId))
                .uniqueResult();
    }

    @Override
    public boolean isExists(String rtxId) {
        Argument.isEmpty(rtxId, "查询员工是否存在时,rtxId不能为空!");
        long count = (Long) createRowCountsCriteria(Employee.class)
                .add(Restrictions.eq("extensionNumber", rtxId))
                .uniqueResult();
        return count > 0;
    }

    @Override
    public Employee findByRtxId(String rtxId) {
        Argument.isEmpty(rtxId, "查询员工时,rtxId不能为空!");
        return (Employee) createCriteria(Employee.class)
                .add(Restrictions.eq("extensionNumber", rtxId))
                .setMaxResults(1)
                .uniqueResult();
    }
}