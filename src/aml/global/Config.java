/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.global;

//import aml.global.Enums.WindowType;
import aml.global.Enums.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Manager of the simulator's XML configuration file
 *
 * @author ddefalco
 */
@XmlRootElement
public class Config {

    private static Config _instance;
    private int fraudPotential;
    private int numberOfNode, maxEdgesPerStep;
    private int maxNumberParents, maxNumberPartners, maxNumberDummies;
    private double employeeMeanHonest, employeeStdDevHonest,
            freelanceMeanHonest, freelanceStdDevHonest,
            bigCompanyMeanHonest, bigCompanyStdDevHonest,
            smallCompanyMeanHonest, smallCompanyStdDevHonest;
    private double employeeMeanLaunderer, employeeStdDevLaunderer,
            freelanceMeanLaunderer, freelanceStdDevLaunderer,
            bigCompanyMeanLaunderer, bigCompanyStdDevLaunderer,
            smallCompanyMeanLaunderer, smallCompanyStdDevLaunderer;
    private double employeeMessageMean, employeeMessageStdDev,
            freelanceMessageMean, freelanceMessageStdDev,
            smallCompanyMessageMean, smallCompanyMessageStdDev,
            bigCompanyMessageMean, bigCompanyMessageStdDev;
    private int laundererPercentage;
    private int yearsNumber;
    private int numberWekaRuns;
    private WindowSize windowSize;
    private double partnerProbability, dummyProbability, parentProbability;
    private boolean guiEnabled;
    private String fileNameTransaction, fileNameEntity,
            dataBaseUsername, dataBasePassword,
            dataBaseConnection, dataBaseDriver;
    private PersistenceMode persistenceMode;

    /**
     * Singleton instance of the class Config rapresents the configuration of
     * the application
     *
     */
    private Config() {
    }

    public static Config instance() {
        if (_instance == null) {
            _instance = unmashall();
            return _instance;
        }
        return _instance;
    }

    /**
     * Unmarshalling of the XML
     *
     * @return
     */
    private static Config unmashall() {
        try {
            File file = new File("." + File.separator + "res" + File.separator + "Config.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return _instance = (Config) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            return new Config();
        }
    }

    /* Properties listed in configuration file */
    @XmlElement
    public int getNumberOfNode() {
        return numberOfNode;
    }

    public void setNumberOfNode(int num) {
        numberOfNode = num;
    }

    @XmlElement
    public int getMaxEdgesPerStep() {
        return maxEdgesPerStep;
    }

    public void setMaxEdgesPerStep(int num) {
        maxEdgesPerStep = num;
    }

    @XmlElement
    public int getMaxNumberPartners() {
        return maxNumberPartners;
    }

    public void setMaxNumberPartners(int num) {
        maxNumberPartners = num;
    }

    @XmlElement
    public int getMaxNumberParents() {
        return maxNumberParents;
    }

    public void setMaxNumberParents(int num) {
        maxNumberParents = num;
    }

    @XmlElement
    public int getMaxNumberDummies() {
        return maxNumberDummies;
    }

    public void setMaxNumberDummies(int numberDummies) {
        this.maxNumberDummies = numberDummies;
    }

    @XmlElement
    public int getLaundererPercentage() {
        return laundererPercentage;
    }

    public void setLaundererPercentage(int laundererPercentage) {
        this.laundererPercentage = laundererPercentage;
    }

    @XmlElement
    public int getFraudPotential() {
        return fraudPotential;
    }

    public void setFraudPotential(int fraudPotential) {
        this.fraudPotential = fraudPotential;
    }

    @XmlElement
    public boolean isGuiEnabled() {
        return guiEnabled;
    }

    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    @XmlElement
    public double getEmployeeMessageMean() {
        return employeeMessageMean;
    }

    public void setEmployeeMessageMean(double employeeMessageMean) {
        this.employeeMessageMean = employeeMessageMean;
    }

    @XmlElement
    public double getEmployeeMessageStdDev() {
        return employeeMessageStdDev;
    }

    public void setEmployeeMessageStdDev(double employeeMessageStdDev) {
        this.employeeMessageStdDev = employeeMessageStdDev;
    }

    @XmlElement
    public double getFreelanceMessageMean() {
        return freelanceMessageMean;
    }

    public void setFreelanceMessageMean(double freelanceMessageMean) {
        this.freelanceMessageMean = freelanceMessageMean;
    }

    @XmlElement
    public double getFreelanceMessageStdDev() {
        return freelanceMessageStdDev;
    }

    public void setFreelanceMessageStdDev(double freelanceMessageStdDev) {
        this.freelanceMessageStdDev = freelanceMessageStdDev;
    }

    @XmlElement
    public double getSmallCompanyMessageMean() {
        return smallCompanyMessageMean;
    }

    public void setSmallCompanyMessageMean(double smallCompanyMessageMean) {
        this.smallCompanyMessageMean = smallCompanyMessageMean;
    }

    @XmlElement
    public double getSmallCompanyMessageStdDev() {
        return smallCompanyMessageStdDev;
    }

    public void setSmallCompanyMessageStdDev(double smallCompanyMessageStdDev) {
        this.smallCompanyMessageStdDev = smallCompanyMessageStdDev;
    }

    @XmlElement
    public double getBigCompanyMessageMean() {
        return bigCompanyMessageMean;
    }

    public void setBigCompanyMessageMean(double bigCompanyMessageMean) {
        this.bigCompanyMessageMean = bigCompanyMessageMean;
    }

    @XmlElement
    public double getBigCompanyMessageStdDev() {
        return bigCompanyMessageStdDev;
    }

    public void setBigCompanyMessageStdDev(double bigCompanyMessageStdDev) {
        this.bigCompanyMessageStdDev = bigCompanyMessageStdDev;
    }

    @XmlElement
    public double getEmployeeMeanHonest() {
        return employeeMeanHonest;
    }

    public void setEmployeeMeanHonest(double num) {
        employeeMeanHonest = num;
    }

    @XmlElement
    public double getEmployeeStdDevHonest() {
        return employeeStdDevHonest;
    }

    public void setEmployeeStdDevHonest(double num) {
        employeeStdDevHonest = num;
    }

    @XmlElement
    public double getFreelanceMeanHonest() {
        return freelanceMeanHonest;
    }

    public void setFreelanceMeanHonest(double num) {
        freelanceMeanHonest = num;
    }

    @XmlElement
    public double getFreelanceStdDevHonest() {
        return freelanceStdDevHonest;
    }

    public void setFreelanceStdDevHonest(double num) {
        freelanceStdDevHonest = num;
    }

    @XmlElement
    public double getSmallCompanyMeanHonest() {
        return smallCompanyMeanHonest;
    }

    public void setSmallCompanyMeanHonest(double num) {
        smallCompanyMeanHonest = num;
    }

    @XmlElement
    public double getSmallCompanyStdDevHonest() {
        return smallCompanyStdDevHonest;
    }

    public void setSmallCompanyStdDevHonest(double num) {
        smallCompanyStdDevHonest = num;
    }

    @XmlElement
    public double getBigCompanyMeanHonest() {
        return bigCompanyMeanHonest;
    }

    public void setBigCompanyMeanHonest(double num) {
        bigCompanyMeanHonest = num;
    }

    @XmlElement
    public double getBigCompanyStdDevHonest() {
        return bigCompanyStdDevHonest;
    }

    public void setBigCompanyStdDevHonest(double num) {
        bigCompanyStdDevHonest = num;
    }

    @XmlElement
    public double getEmployeeMeanLaunderer() {
        return employeeMeanLaunderer;
    }

    public void setEmployeeMeanLaunderer(double num) {
        employeeMeanLaunderer = num;
    }

    @XmlElement
    public double getEmployeeStdDevLaunderer() {
        return employeeStdDevLaunderer;
    }

    public void setEmployeeStdDevLaunderer(double num) {
        employeeStdDevLaunderer = num;
    }

    @XmlElement
    public double getFreelanceMeanLaunderer() {
        return freelanceMeanLaunderer;
    }

    public void setFreelanceMeanLaunderer(double num) {
        freelanceMeanLaunderer = num;
    }

    @XmlElement
    public double getFreelanceStdDevLaunderer() {
        return freelanceStdDevLaunderer;
    }

    public void setFreelanceStdDevLaunderer(double num) {
        freelanceStdDevLaunderer = num;
    }

    @XmlElement
    public double getSmallCompanyMeanLaunderer() {
        return smallCompanyMeanLaunderer;
    }

    public void setSmallCompanyMeanLaunderer(double num) {
        smallCompanyMeanLaunderer = num;
    }

    @XmlElement
    public double getSmallCompanyStdDevLaunderer() {
        return smallCompanyStdDevLaunderer;
    }

    public void setSmallCompanyStdDevLaunderer(double num) {
        smallCompanyStdDevLaunderer = num;
    }

    @XmlElement
    public double getBigCompanyMeanLaunderer() {
        return bigCompanyMeanLaunderer;
    }

    public void setBigCompanyMeanLaunderer(double num) {
        bigCompanyMeanLaunderer = num;
    }

    @XmlElement
    public double getBigCompanyStdDevLaunderer() {
        return bigCompanyStdDevLaunderer;
    }

    public void setBigCompanyStdDevLaunderer(double num) {
        bigCompanyStdDevLaunderer = num;
    }

    @XmlElement
    public double getPartnerProbability() {
        return partnerProbability;
    }

    public void setPartnerProbability(double partnerProbability) {
        this.partnerProbability = partnerProbability;
    }

    @XmlElement
    public double getDummyProbability() {
        return dummyProbability;
    }

    public void setDummyProbability(double dummyProbability) {
        this.dummyProbability = dummyProbability;
    }

    @XmlElement
    public double getParentProbability() {
        return parentProbability;
    }

    public void setParentProbability(double parentProbability) {
        this.parentProbability = parentProbability;
    }

    @XmlElement
    public PersistenceMode getPersistenceMode() {
        return this.persistenceMode;
    }

    public void setPersistenceMode(PersistenceMode mode) {
        this.persistenceMode = mode;
    }

    @XmlElement
    public int getNumberWekaRuns() {
        return numberWekaRuns;
    }

    public void setNumberWekaRuns(int numberWekaRuns) {
        this.numberWekaRuns = numberWekaRuns;
    }

    @XmlElement
    public int getYearsNumber() {
        return yearsNumber;
    }

    public void setYearsNumber(int yearsNumber) {
        this.yearsNumber = yearsNumber;
    }

    @XmlElement
    public WindowSize getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(WindowSize windowSize) {
        this.windowSize = windowSize;
    }

    @XmlElement
    public String getFileNameTransaction() {
        return fileNameTransaction;
    }

    public void setFileNameTransaction(String fileNameTransaction) {
        this.fileNameTransaction = fileNameTransaction;
    }

    @XmlElement
    public String getFileNameEntity() {
        return fileNameEntity;
    }

    public void setFileNameEntity(String fileNameEntity) {
        this.fileNameEntity = fileNameEntity;
    }

    @XmlElement
    public String getDataBaseUsername() {
        return dataBaseUsername;
    }

    public void setDataBaseUsername(String dataBaseUsername) {
        this.dataBaseUsername = dataBaseUsername;
    }

    @XmlElement
    public String getDataBasePassword() {
        return dataBasePassword;
    }

    public void setDataBasePassword(String dataBasePassword) {
        this.dataBasePassword = dataBasePassword;
    }

    @XmlElement
    public String getDataBaseConnection() {
        return dataBaseConnection;
    }

    public void setDataBaseConnection(String dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @XmlElement
    public String getDataBaseDriver() {
        return dataBaseDriver;
    }

    public void setDataBaseDriver(String dataBaseDriver) {
        this.dataBaseDriver = dataBaseDriver;
    }

}
