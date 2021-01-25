int strdiff(char* a, char* b)
{
	int hh1, hh2;
	char hs1, hs2;
	hh1 = hh2 =1;
	if (strlen(a) == 0 || strlen(b) == 0)
		return 0;
	if (strcmp(a, b) == 0)
		return -1;
	else
	{
		hs1 = *a;
		hs2 = *b;
		while ((hs1 & 1) == (hs2 & 1))
		{
			hh1 +=1;
			hs1 = hs1 >> 1;
			hs2 = hs2 >> 1;
			if (!(hh1 % 8))
			{
				hs1 = *(a + hh2);
				hs2 = *(b + hh2);
				hh2 += 1;
			}
		}
	}
	return hh1;
}