package org.trainings.charan.springjdbch2.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.trainings.charan.springjdbch2.constants.EmployeeConstants;
import org.trainings.charan.springjdbch2.model.Address;
import org.trainings.charan.springjdbch2.model.EmployeeVO;
@Repository
public class EmployeeVODAO {
@Autowired
private JdbcTemplate jdbcTemplate;
public EmployeeVO getEmployeeVOById(Integer id) {
	EmployeeVO empVO=jdbcTemplate.queryForObject(EmployeeConstants.GET_EMPLOYEE_BY_ID,new Object[] {id}, prepareRowMapper());
	List<Address> addresses=jdbcTemplate.query("select * from address where employee_id=?",new Object[] {id},new BeanPropertyRowMapper<>(Address.class));
	empVO.setAddresses(addresses);
	return empVO;
}

private RowMapper<EmployeeVO> prepareRowMapper() {
	return new RowMapper<EmployeeVO>() {
		@Override
		public EmployeeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmployeeVO employee = new EmployeeVO();
			employee.setId(rs.getInt("ID"));
			employee.setName(rs.getString("NAME"));
			employee.setEmail(rs.getString("EMAIL"));
			employee.setDob(rs.getDate("DOB"));
			return employee;

		}

	};
}
public void addEmployeeVO(EmployeeVO empVO) {
	jdbcTemplate.update(EmployeeConstants.INSERT_EMPLOYEE,empVO.getName(),empVO.getDob(),empVO.getEmail());
	EmployeeVO emp=jdbcTemplate.queryForObject(EmployeeConstants.QGET_EMPLOYEE_BY_NAME_AND_EMAIL,new Object[] {empVO.getName(),empVO.getEmail()}, prepareRowMapper());

for(Address address:empVO.getAddresses()) {
	jdbcTemplate.update(EmployeeConstants.INSERT_ADDRESS,
			emp.getId(),
			address.getStreet(),
			address.getCity(),
			address.getState(),
			address.getPincode());
}

}
}