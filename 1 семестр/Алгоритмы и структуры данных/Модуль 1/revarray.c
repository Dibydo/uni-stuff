void revarray(void *base, unsigned long nel, unsigned long width)
{
	unsigned char *hc1, *hc2, hc3;
	for (int i = 0; i < nel / 2; i++)
		for (int j = 0; j < width; j++)
		{
			hc1 = ((char*)base + width * i + j);
			hc2 = ((char*)base + width * (nel - i - 1) + j);
			hc3 = *hc1;
			*hc1 = *hc2;
			*hc2 = hc3;
		}
}