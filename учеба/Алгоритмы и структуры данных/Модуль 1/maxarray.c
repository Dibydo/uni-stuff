int maxarray(void  *base, unsigned long nel, unsigned long width, int (*compare)(void *a, void *b))
{
	int rembi, hh;
	rembi = 0;
	for (int i = 0; i < nel - 1; ++i)
	{
		hh = (compare((char*)base + rembi * width, (char*)base + i * width));
		if (hh < 0)
			rembi = i;
	}
	return rembi;
}