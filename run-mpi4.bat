copy input  "..\domain1"
copy input  "..\domain2"
copy input  "..\domain3"
copy 2D_to_3D	"..\domain1"
copy 2D_to_3D	"..\domain2"
copy 2D_to_3D	"..\domain3"
mpiexec -n 4 EMSolBatch_MPI.exe -f input