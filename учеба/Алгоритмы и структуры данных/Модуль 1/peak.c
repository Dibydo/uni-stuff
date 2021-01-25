unsigned long peak(unsigned long nel, int (*less)(unsigned long i, unsigned long j))
{
	int yep;
	unsigned long hh1, hh2, hh3, hh4, hh5;
	yep = hh1 = hh2 = hh3 = hh5 = 0;
	hh4 = nel - 1;
	while (hh1 < hh4)
	{
		hh3 = hh1 + (hh4 - hh1) / 2;
		hh2 = hh3 + 1;
		hh5 = hh3 - 1;
		yep = less(hh5, hh3);
		if (yep == 0)
			hh4 = hh3;
		else
		{
			yep = less(hh3, hh2);
			if (yep == 1)
				hh1 = hh3;
			else break;
		}
	}
	return hh3;
}