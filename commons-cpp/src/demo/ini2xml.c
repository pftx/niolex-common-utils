#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>

#define SIZE 1024

int
main (int argc, char *argv[])
{
  FILE *from, *to;
  char buf[SIZE];
  char secname[SIZE];
  char *msg, *ptr;
  int err, len, is_in_sec = 0, i;

  if (argc != 3)
    {
      printf ("Usage: %s <from> <to>\n\t2 arguments expected, actual: %d\n",
              argv[0], argc - 1);
      return -1;
    }

  from = fopen (argv[1], "r");
  if (from == NULL)
    {
      err = errno;
      msg = strerror (err);
      printf ("Failed to open file <%s>: (%d) %s\n", argv[1], err, msg);
    }

  to = fopen (argv[2], "w");
  if (to == NULL)
    {
      err = errno;
      msg = strerror (err);
      printf ("Failed to open file <%s>: (%d) %s\n", argv[2], err, msg);
    }

  while (NULL != fgets (buf, SIZE, from))
    {
      len = strlen (buf);
      if (len == SIZE - 1)
        {
          printf ("Line too long: %s...\n\t exit...", buf);
          return -2;
        }

      /* Check empty line. */
      for (i = 0; i < len; ++i)
        {
          if (!isspace (buf[i]))
            break;
        }
      if (i == len)
        {
          if (is_in_sec)
            {
              fprintf (to, "</%s>\n", secname);
              is_in_sec = 0;
            }
          fprintf (to, "\n");
          continue;
        }

      /* Remove '\n'. */
      buf[len - 1] = '\0';

      /* Check section. */
      if (buf[0] == '[')        /* Section Name. */
        {
          buf[len - 2] = '\0';
          if (is_in_sec)
            {
              fprintf (to, "</%s>\n", secname);
            }
          else
            {
              is_in_sec = 1;
            }
          strcpy (secname, buf + 1);
          fprintf (to, "<%s>\n", secname);
          continue;
        }

      if (buf[0] == ';')        /* Comment. */
        {
          fprintf (to, "<!-- %s -->\n", buf + 1);
          continue;
        }

      /* Check property line. */
      ptr = strchr (buf, '=');
      if (ptr == NULL)
        {
          printf ("Invalid line format: %s\n\t exit...", buf);
          return -3;
        }

      *ptr = ' ';

      ptr = strchr (buf, ' ');
      if (ptr == NULL)
        {
          printf ("Invalid line format: %s\n\t exit...", buf);
          return -4;
        }

      *ptr = '\0';
      ++ptr;

      while (isspace (*ptr))
        {
          ++ptr;
        }

      if (*ptr == '\0')
        {
          printf ("Invalid line format: %s\n\t exit...", buf);
          return -5;
        }

      fprintf (to, "    <%s>%s</%s>\n", buf, ptr, buf);
    }

  if (is_in_sec)
    {
      fprintf (to, "</%s>\n", secname);
    }

  fclose (from);
  fclose (to);

  return 0;
}