copy input  "..\domain1"
copy  2D_to_3D	"..\domain1"
mpiexec -n 2 EMSolBatch_MPI.exe -f input