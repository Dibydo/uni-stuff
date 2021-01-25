unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i))
{
        unsigned long h1, h2, h3;
        int hh;
        h1 = 0;
        h2 = nel-1;
        for (int i = 0; i < nel; i++)
        {
                h3 = (h1 + h2 + 1) / 2;
                hh = compare(h3);
                if (hh == 0) return h3;
                        else if (hh == 1) h2 = h3 - 1; 
                                else if (hh == -1) h1 = h3 + 1;
                                        else return nel;
        }
}