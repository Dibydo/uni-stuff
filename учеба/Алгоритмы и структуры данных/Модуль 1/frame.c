#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int main(int argc, char** argv)
{
	int hh1, hh2, i, j;
	hh1 = hh2 = i = j;
	if (argv[3] == NULL)
	{
		printf("Usage: frame <height> <width> <text>");
		return 0;
	}
	hh1 = (atoi(argv[2]) - strlen(argv[3])) / 2;
	if (hh1 <= 0 || atoi(argv[1]) <= 2)
	{
		printf("Error");
		return 0;
	}
	hh2 = ((atoi(argv[1]) + 1) / 2);
	for (j = 1; j <= atoi(argv[1]); j++)
	{
		if (j == 1 || j == atoii(argv[1]))
			for (i = 0; i < atoi(argv[2]); i++)
				printf("*");
		else
		{
			if (j == hh2)
			{
				printf("*");
				for (i = 1; i <= hh1 - 1; i++)
					printf(" ");
				printf("%s", argv[3]);
				for (i = hh1 + strlen(argv[3]) + 1; i <= atoi(argv[2]); i++)
					if (i == atoi(argv[2]))
						printf("*");
					else printf(" ");
			}
			else
			{
				for (i = 1; i <= atoi(argv[2]); i++)
				{
					if (i == 1 || i == atoi(argv[2]))
						printf("*");
					else printf(" ");
				}
			}
		}
		printf("\n");
	}
	return 0;
}