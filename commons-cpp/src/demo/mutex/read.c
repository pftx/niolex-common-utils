#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/shm.h>
#include <sys/stat.h>

int main ()
{
  key_t shm_key = 6166529;
  const int shm_size = 1024;

  int shm_id, err_id;
  char* shmaddr, *ptr;
  pthread_mutex_t *mutex_p;
  
  /* Allocate a shared memory segment. */
  shm_id = shmget(shm_key, shm_size, IPC_CREAT | S_IRUSR | S_IWUSR);

  /* Attach the shared memory segment. */
  shmaddr = (char*) shmat(shm_id, 0, 0);

  printf("shared memory attached at address %p\n", shmaddr);

  /* Start to read data. */
  mutex_p = (pthread_mutex_t *)shmaddr;
  ptr = shmaddr + sizeof (pthread_mutex_t);

  err_id = pthread_mutex_trylock(mutex_p);
  if (err_id == EBUSY)
  {
    printf("Failed to get lock - %d: %s\n", err_id, ptr);
  }

  err_id = pthread_mutex_lock(mutex_p);
  if (err_id == 0)
  {
    printf("Mutex locked reader.\n");
  }
  pthread_mutex_unlock(mutex_p);
  printf("Mutex unlocked reader.\n");

  /* Detach the shared memory segment. */
  shmdt(shmaddr);
  return 0;
}
