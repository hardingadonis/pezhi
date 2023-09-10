package dao;

import java.time.LocalDate;
import java.util.List;

public interface ITransaction<T> {
    public List<T> listToday();

    public List<T> listDate(Integer year, Integer month, Integer day);

    public List<T> listDate(LocalDate date);

    public List<T> listMonth(Integer year, Integer month);

    public List<T> listYear(Integer year);

    public List<T> listPeriod(LocalDate begin, LocalDate end);
}