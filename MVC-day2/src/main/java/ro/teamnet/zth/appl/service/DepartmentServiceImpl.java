package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.dao.DepartmentDao;
import ro.teamnet.zth.appl.domain.Department;

import java.util.List;

/**
 * Created by Cristian.Barbu on 7/24/2017.
 */
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao = new DepartmentDao();

    @Override
    public List<Department> findAll() {
        return departmentDao.findAllDepartments();
    }

    @Override
    public Department findOne(Long departmentId) {
        return departmentDao.findDepartmentById(departmentId);
    }

    @Override
    public Boolean delete(Long departmentId) {
        Department departmentToDelete = departmentDao.findDepartmentById(departmentId);
        if (departmentToDelete == null) {
            return false;
        }
        departmentDao.deleteDepartment(departmentToDelete);
        return true;
    }

    @Override
    public Department save(Department department) {
        return departmentDao.insertDepartment(department);
    }

    @Override
    public Department update(Department department) {
        return departmentDao.updateDepartment(department);
    }
}
