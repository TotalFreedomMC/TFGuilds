package me.totalfreedom.tfguilds.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaginationList<T> extends ArrayList<T>
{

    private final int epp;

    /**
     * Defines a new Pagination List.
     *
     * @param epp Elements per page - how many elements will be included on a page of the list.
     */
    public PaginationList(int epp)
    {
        super();
        this.epp = epp;
    }

    /**
     * Defines a new Pagination List.
     *
     * @param epp      Elements per page - how many elements will be included on a page of the list.
     * @param elements Elements to add to the list immediately.
     */
    @SafeVarargs
    public PaginationList(int epp, T... elements)
    {
        super(Arrays.asList(elements));
        this.epp = epp;
    }

    /**
     * @return The number of pages this list holds.
     */
    public int getPageCount()
    {
        return (int)Math.ceil((double)size() / (double)epp);
    }

    /**
     * Get a page from the list.
     *
     * @param page Page you want to access.
     * @return A sublist of only the elements from that page.
     */
    public List<T> getPage(int page)
    {
        if (page < 1 || page > getPageCount())
        {
            return null;
        }
        int startIndex = (page - 1) * epp;
        int endIndex = Math.min(startIndex + (epp - 1), this.size() - 1);
        return subList(startIndex, endIndex + 1);
    }

    /*@Override
    public String toString()
    {
        StringBuilder res = new StringBuilder();
        for (int i = 1; i <= getPageCount(); i++)
        {
            res.append("Page ").append(i).append(": ").append("\n");
            for (T element : getPage(i))
                res.append(" - ").append(element).append("\n");
        }
        return res.toString();
    }*/
}
