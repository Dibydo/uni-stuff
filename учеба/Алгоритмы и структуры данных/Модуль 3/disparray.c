#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Yep Yep;

struct Yep
{
	long kval;
	long val;
	struct Yep* bel;
};

Yep* LS1(Yep* arr, long kval)
{
	Yep* hh = arr;
	while (hh != NULL && (hh -> kval != kval))
		hh = hh -> bel;
	return hh;
}

Yep* LS2(Yep* arr, long valk)
{
	Yep* hh = arr;
	while (hh != NULL && hh -> bel != NULL && hh -> kval != valk)
		hh = hh -> bel;
	return hh;
}

Yep* ins_op(Yep* arr, long pk, long pv, Yep** arr_, int hh_)
{
	Yep* hh;
	if (arr != NULL && (arr -> kval == pk))
		arr -> val = pv;
	else
	{
		if (arr != NULL)
		{
			arr -> bel = (Yep*)malloc(sizeof(Yep));
			arr = arr -> bel;
		}
		else
		{
			arr_[pk % hh_] = (struct Yep*)malloc(sizeof(struct Yep));
			arr = arr_[pk % hh_];
		}
		arr -> kval = pk;
		arr -> val = pv;
		arr -> bel = NULL;
	}
	return arr;
}

void m_free(long len, Yep** inarr)
{
	for (long i = 0; i < len; i++)
	{
		Yep* harr = inarr[i];
		for (;harr != NULL;)
		{
			Yep* hv = harr;
			harr = harr -> bel;
			free(hv);
		}
		free(harr);
	}
	free(inarr);
}

int main()
{
	long int hh1, hh2, hh3, hh4;
	char task[8];
	scanf("%ld%ld", &hh1, &hh2);
	struct Yep** harr = calloc(hh2, sizeof(struct Yep*)), *hv1;
	for (long int i = 0; i < hh1; i++)
	{
		scanf("%s", &task);
		if (strcmp(task, "AT") == 0)
		{
			scanf("%ld", &hh3);
			Yep* hih;
			hih = harr[hh3 % hh2];
			hv1 = LS1(hih, hh3);
			if (hv1 != 0)
				printf("%ld\n", hv1 -> val);
			else printf("0\n");
		}
		else
		{
			scanf("%ld %ld", &hh3, &hh4);
			if (hh4 != 0)
			{
				hv1 = harr[hh3 % hh2];
				hv1 = LS2(hv1, hh3);
				hv1 = ins_op(hv1, hh3, hh4, harr, hh2);
			}
			else
			{
				hv1 = harr[hh3 % hh2];
				if (hv1 != NULL)
				{
					if (hv1 -> kval == hh3)
					{
						Yep* hihi = hv1;
						harr[hh3 % hh2] = hv1 -> bel;
						free(hihi);
					}
					else
					{
						while (hv1 -> bel && (hv1 -> bel -> kval != hh3))
							hv1 = hv1 -> bel;
						if (hv1 -> bel != NULL)
						{
							Yep* hihih = hv1 -> bel;
							hv1 -> bel = hv1 -> bel -> bel;
							free(hihih);
						}
					}
				}
			}
		}
	}
	m_free(hh2, harr);
	return 0;
}