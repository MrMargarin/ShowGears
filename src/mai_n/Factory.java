package mai_n;

import dao.dao_interface.*;
import dao.dao_impl.*;
/**
 * Created by Dmitrey on 07.09.2015.
 */
public class Factory {

    private static stipp_dao _stipp_dao = null;
    private static stus_dao _stus_dao = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance()
    {
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }

    public stipp_dao get_stipp_dao()
    {
        if(_stipp_dao == null)
        {
            _stipp_dao = new stipp_daoImpl();
        }
        return _stipp_dao;
    }

    public stus_dao get_stus_dao()
    {
        if(_stus_dao == null)
        {
            _stus_dao = new stus_daoImpl();
        }
        return _stus_dao;
    }




}
