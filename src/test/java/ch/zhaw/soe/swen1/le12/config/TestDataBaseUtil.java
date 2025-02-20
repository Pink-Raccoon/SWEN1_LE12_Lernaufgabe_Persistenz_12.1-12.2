package ch.zhaw.soe.swen1.le12.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for DataBaseUtil class
 */
public class TestDataBaseUtil {


	@BeforeEach
	public void setUp() {

	}

	@Test
	public void connectionEstablished() throws SQLException {
		assertNotNull(DataBaseUtil.getConn());
		assertTrue(DataBaseUtil.getConn().isValid(1));
	}

	@Test
	public void connectionInfo() throws SQLException {
		String user = DataBaseUtil.getConn().getMetaData().getUserName();
		String url = DataBaseUtil.getConn().getMetaData().getURL();
		Assert.assertEquals("SA", user);
		Assert.assertEquals("jdbc:h2:mem:test", url);
	}

}
