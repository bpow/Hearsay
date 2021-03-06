package org.renci.hearsay.ws.impl;

import org.renci.hearsay.dao.HearsayDAOException;
import org.renci.hearsay.dao.SimpleAlleleDAO;
import org.renci.hearsay.dao.model.SimpleAllele;
import org.renci.hearsay.ws.SimpleAlleleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAlleleServiceImpl implements SimpleAlleleService {

    private final Logger logger = LoggerFactory.getLogger(SimpleAlleleServiceImpl.class);

    private SimpleAlleleDAO simpleAlleleDAO;

    public SimpleAlleleServiceImpl() {
        super();
    }

    @Override
    public SimpleAllele findById(Long id) {
        logger.debug("ENTERING findById(Long)");
        SimpleAllele ret = null;
        try {
            ret = simpleAlleleDAO.findById(id);
        } catch (HearsayDAOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public SimpleAlleleDAO getSimpleAlleleDAO() {
        return simpleAlleleDAO;
    }

    public void setSimpleAlleleDAO(SimpleAlleleDAO simpleAlleleDAO) {
        this.simpleAlleleDAO = simpleAlleleDAO;
    }

}
