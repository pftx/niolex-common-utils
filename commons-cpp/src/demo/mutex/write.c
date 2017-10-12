#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/shm.h>
#include <sys/stat.h>

int main ()
{
  key_t shm_key = 6166529;
  const int shm_size = 1024;

  int shm_id, err_id = 0;
  char* shmaddr, *ptr;
  pthread_mutex_t *mutex_p;
  pthread_mutexattr_t m_attr;

  printf("writer started.\n");

  /* Allocate a shared memory segment. */
  shm_id = shmget(shm_key, shm_size, IPC_CREAT | S_IRUSR | S_IWUSR);

  /* Attach the shared memory segment. */
  shmaddr = (char*) shmat(shm_id, 0, 0);
  printf("shared memory attached at address %p\n", shmaddr);

  /* Start to write data. */
  mutex_p = (pthread_mutex_t *) shmaddr;
  err_id |= pthread_mutexattr_init(&m_attr);
  err_id |= pthread_mutexattr_setpshared(&m_attr, PTHREAD_PROCESS_SHARED);
  err_id |= pthread_mutex_init(mutex_p, &m_attr);
  //err_id |= pthread_mutex_init(mutex_p, NULL);
  if (err_id != 0)
  {
    printf("Failed to init mutex - %d.\n", err_id);
    exit(-1);
  }

  ptr = shmaddr + sizeof (pthread_mutex_t);
  sprintf(ptr, "mutex added.");
  printf("writer ended.\n");

  pthread_mutex_lock(mutex_p);
  printf("Mutex locked.\n");

  /*calling the other process*/
  system("./reader&");

  /*sleep 5 seconds.*/
  sleep(5);
  printf("Sleep ended.\n");
  pthread_mutex_unlock(mutex_p);
  printf("Mutex unlocked.\n");

  pthread_mutexattr_destroy(&m_attr);
  pthread_mutex_destroy(mutex_p);

  /* Detach the shared memory segment. */
  shmdt(shmaddr);
  /* Deallocate the shared memory segment.*/
  shmctl(shm_id, IPC_RMID, 0);

  return 0;
}
