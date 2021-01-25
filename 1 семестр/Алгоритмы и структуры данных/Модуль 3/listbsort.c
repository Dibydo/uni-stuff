#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct term term;

struct term
{
	struct term* smth;
	char* wd;
};

term* list_cons(char* line)
{
	term* br = calloc(1, sizeof(term));
	br -> wd = calloc(100, sizeof(char));
	strcpy(br -> wd, line);
	br -> smth = NULL;
	return br;
}

void insert_spec(term* list, char* added)
{
	term* hh1;
	hh1 = list_cons(added);
	term* hh2;
	while (list != NULL)
	{
		hh2 = list;
		list = list -> smth;
	}
	hh2 -> smth = hh1;
}

int comparer(term* f, term* s)
{
	if (strlen(f -> wd) > strlen(s -> wd))
		return 1;
	else return 0;
}

void bub_list_sort(term* input)
{
	term* l1;
	term* l2;
	for (l1 = input -> smth; l1 != NULL; l1 = l1 -> smth)
		for (l2 = input -> smth; l2 -> smth != NULL; l2 = l2 -> smth)
			if (comparer(l2, l2 -> smth) == 1)
			{
				char* sup;
				sup = l2 -> wd;
				l2 -> wd = l2 -> smth -> wd;
				l2 -> smth -> wd = sup;
			}
}

int main()
{
	long int hh_1, hh_2, counter1, counter2;
	char* hs = calloc(10000, sizeof(char));
	char* ph = calloc(1000, sizeof(char));
	term* hl1 = list_cons("");
	term* hl2 = list_cons("");
	term* hv1;
	term* hv2;
	gets(hs);
	for (counter1 = 0, counter2 = 0; hs[counter1] != '\0'; counter1++)
	{
		if (hs[counter1] != ' ')
		{
			ph[counter2] = hs[counter1];
			counter2++;
		}
		if (hs[counter1] == ' ' && counter2 > 0)
		{
			insert_spec(hl1, ph);
			for (hh_1 = 0; hh_1 < counter2; hh_1++)
			{
				ph[hh_1] = 0;
			}
			counter2 = 0;
		}
	}
	if (counter2 != 0)
	{
		insert_spec(hl1, ph);
		for (hh_1 = 0; hh_1 < counter2; hh_1++)
		{
			ph[hh_1] = 0;
		}
		counter2 = 0;
	}
	free(hs);
	free(ph);
	bub_list_sort(hl1);
	hv2 = hl1 -> smth;
	while (hv2 != NULL)
	{
		hv1 = hv2;
		printf("%s ", hv2 -> wd);
		free(hv2 -> wd);
		hv2 = hv2 -> smth;
		free(hv1);
	}
	free(hl2 -> wd);
	free(hl2);
	free(hl1 -> wd);
	free(hl1);
	return 0;
}