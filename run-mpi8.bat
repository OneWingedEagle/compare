copy input  "..\domain1"
copy input  "..\domain2"
copy input  "..\domain3"
copy input  "..\domain4"
copy input  "..\domain5"
copy input  "..\domain6"
copy input  "..\domain7"
copy 2D_to_3D	"..\domain1"
copy 2D_to_3D	"..\domain2"
copy 2D_to_3D	"..\domain3"
copy 2D_to_3D	"..\domain4"
copy 2D_to_3D	"..\domain5"
copy 2D_to_3D	"..\domain6"
copy 2D_to_3D	"..\domain7"
mpiexec -n 8 EMSolBatch_MPI.exe -f input
