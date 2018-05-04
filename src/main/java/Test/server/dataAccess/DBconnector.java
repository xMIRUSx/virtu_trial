package Test.server.dataAccess;

import Test.shared.entities.EstateObject;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import Test.shared.entities.Client;
import Test.shared.entities.Contract;

import java.util.List;
import java.util.Map;

/**
 * ОБъект для связи с базой данных
 */
public class DBconnector {
    private static SessionFactory sessionFactory;
    private static DBconnector instance = null;

    private DBconnector(){
        try {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static DBconnector getInstance() {
        if (instance == null){
            instance = new DBconnector();
        }
        return instance;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Вставить сущность в БД
     * @param o сущность
     * @throws HibernateException
     */
    public void insertData(Object o) throws HibernateException{
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        try {
            s.save(o);
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
            finally {
            s.close();
        }
    }

    /**
     * Обновить сущность в БД
     * @param o сущность
     * @throws HibernateException
     */
    public void updateData(Object o) throws HibernateException{
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        try {
            s.update(o);
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }
    }

    /**
     * Сохранить или обновить клиента в БД
     * @param c клиент
     * @return id сгенерированная при вставке клиента или id обновляемого клиента
     * @throws HibernateException
     */
    public Long saveClient(Client c) throws HibernateException{
        Long id = null;
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        try {
            if (c.getId() == null){
                id = (Long) s.save(c);
            } else {
                s.update(c);
                id = c.getId();
            }
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }
        return id;
    }

    /**
     * Получить договор по его номеру
     * @param num номер договора
     * @return сущность договора
     */
    public Contract getContractByNum(Long num){
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        Contract result;
        try{
            Query q = s.createQuery( "from Contract where num = :num");
            q.setParameter("num", num);
            result = (Contract) q.uniqueResult();
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }
        return result;
    }

    /**
     * Проверка на налилче в БД договора
     * @param num номер проверяемого договора
     * @return true or false
     * @throws HibernateException
     */
    public boolean contractExists(Long num) throws HibernateException{
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        try{
            Contract c = s.load(Contract.class, num);
            return c.getNum() == num;
        } catch(ObjectNotFoundException e){
            return false;
        } catch(HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }
    }

    /**
     * Проверка на налилче в БД клиента
     * @param client экземпляр клиента, наличие которого надо проверить
     * @return true or false
     * @throws HibernateException
     */
    public boolean clientExists(Client client) throws HibernateException{
        Session s = sessionFactory.openSession();
        int series = client.getPassportSeries();
        int num = client.getPassportNumber();
        Long id = client.getId();
        if (id == null){
            id = Long.valueOf(-1);
        }
        s.beginTransaction();
        try{
            Query q = s.createQuery( "from Client where passportSeries = :series and passportNumber = :num and id <> :id");
            q.setParameter("series", series);
            q.setParameter("num", num);
            q.setParameter("id", id);
            Client result = (Client) q.uniqueResult();
            return result != null;
        } catch(HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }
    }

    public Long searchEstateObject(EstateObject eo) throws HibernateException{
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        try{
            EstateObject dbObject = s.load(EstateObject.class, eo.getAddressPK());
            return dbObject.getId();
        } catch(ObjectNotFoundException e){
            return null;
        }
        finally {
            s.close();
        }
    }

    /**
     * Возвращает список договоров в краткой форме. Из БД берутся только указанные поля.
     * @return список договорв. Договор представлен в виде пар "имя параметра"-"значение параметра"
     */
    public List<Map<String, Object>> getContractsShort(){
        Session s = sessionFactory.openSession();
        s.beginTransaction();
        Query q = s.createQuery( "select new map(con.num as num, con.contractDate as contractDate, " +
                "con.premium as premium, con.startDate as startDate, con.endDate as endDate, " +
                "cl.firstName as firstName, cl.lastName as lastName, cl.patronymic as patronymic)" +
                "  from Contract as con inner join con.client as cl" );

        List<Map<String, Object>> result;
        try {
            result = q.list();
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
            finally {
            s.close();
        }
        return result;
    }

    /**
     * Поиск клиентов по критериям.
     * @param firstName
     * @param lastName
     * @param patronymic
     * @return список клиентов, удовлетворяющих криетриям.
     */
    public List<Client> getClientsByName(String firstName, String lastName, String patronymic){
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        Query q = s.createQuery( "from Client where firstName like :fn and lastName like :ln and patronymic like :pn" );
        try {
            q.setParameter("fn", "%" + firstName + "%");
        } catch (NullPointerException e){
            q.setParameter("fn", "%%");
        }
        try{
        q.setParameter("ln", "%"+lastName+"%");
        } catch (NullPointerException e){
            q.setParameter("ln", "%%");
        }
        try{
            q.setParameter("pn", "%"+patronymic+"%");
        } catch (NullPointerException e){
            q.setParameter("pn", "%%");
        }
        List result = q.list();

        s.getTransaction().commit();
        s.close();

        return result;
    }


    /**
     * Получение коэффициента для расчёта премии.
     * @param area
     * @return
     * @throws NonUniqueResultException
     * @throws IllegalStateException
     */
    public float getAreaCoef(float area) throws NonUniqueResultException, IllegalStateException {
        Session s = sessionFactory.openSession();

        Query query = s.createQuery("select coefficient from AreaCoefficient where :area between coalesce(areaLB, :area) and coalesce(areaUB, :area)");
        query.setParameter("area", area);
        s.beginTransaction();

        // Аргументу должен соответствовать строго один коэффециент.
        // Если строк несколько, метод кинет NonUniqueResultException.
        Float coef;
        try {
            coef = (Float) query.uniqueResult();
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }

        // Если строк нет также кидаем исключение.
        if (coef == null){
            throw new IllegalStateException("AreaCoefficient not found.");
        }

        return coef;
    }

    /**
     * Получение коэффициента для расчёта премии.
     * @param buildYear
     * @return
     * @throws NonUniqueResultException
     * @throws IllegalStateException
     */
    public float getBuildYearCoef(int buildYear) throws NonUniqueResultException, IllegalStateException {
        Session s = sessionFactory.openSession();

        Query query = s.createQuery("select coefficient from BuildYearCoefficient where :buildYear between coalesce(buildYearLB, :buildYear) and coalesce(buildYearUB, :buildYear)");
        query.setParameter("buildYear", buildYear);
        s.beginTransaction();

        // Аргументу должен соответствовать строго один коэффециент.
        // Если строк несколько, метод кинет NonUniqueResultException.
        Float coef;
        try {
            coef = (Float) query.uniqueResult();
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }


        // Если строк нет также кидаем исключение.
        if (coef == null){
            throw new IllegalStateException("BuildYearCoefficient not found.");
        }

        return coef;
    }

    /**
     * Получение коэффициента для расчёта премии.
     * @param type
     * @return
     * @throws NonUniqueResultException
     * @throws IllegalStateException
     */
    public float getEstateTypeCoef(String type) throws NonUniqueResultException, IllegalStateException {
        Session s = sessionFactory.openSession();

        Query query = s.createQuery("select coefficient from EstateTypeCoefficient where type = :type");
        query.setParameter("type", type);
        s.beginTransaction();

        // Аргументу должен соответствовать строго один коэффециент.
        // Если строк несколько, метод кинет NonUniqueResultException.
        Float coef;
        try {
            coef = (Float) query.uniqueResult();
            s.getTransaction().commit();
        } catch (HibernateException e){
            s.getTransaction().rollback();
            throw e;
        }
        finally {
            s.close();
        }

        // Если строк нет также кидаем исключение.
        if (coef == null){
            throw new IllegalStateException("BuildYearCoefficient not found.");
        }

        return coef;
    }



}
