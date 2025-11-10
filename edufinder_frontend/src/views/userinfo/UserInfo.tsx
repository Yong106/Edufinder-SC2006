import { useEffect, useState } from 'react';
import { useAuth } from 'src/context/AuthProvider.tsx';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import { Table, TableBody, TableCell, TableRow } from 'flowbite-react';
import CardBox from 'src/components/shared/CardBox.tsx';
import LocationMap from 'src/views/dashboards/LocationMap.tsx';

const UserInfo = () => {
  const {user, setUser} = useAuth();
  const [postalCode, setPostalCode] = useState(user?.postalCode || "");
  const [originalPostalCode, setOriginalPostalCode] = useState(user?.postalCode || "");
  const [isEditingPostalCode, setIsEditingPostalCode] = useState<boolean>(false);

  useEffect(() => {
    setOriginalPostalCode(user?.postalCode || "");
  }, [postalCode]);

  const handleUpdate = async () => {
    try {
      const res = await fetch (CONSTANTS.backendEndpoint + '/users', {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ postalCode })
      });

      if (!res.ok) throw new Error('Failed to update postal code');

      const updatedUser = await res.json();
      setUser(updatedUser);
      setIsEditingPostalCode(false);
      localStorage.setItem('postalCode', updatedUser.postalCode);
      toast.success('Postal code updated successfully.');
    } catch (err) {
      toast.error("Failed to update postal code");
    }
  }

  const handleCancelUpdate = () => {
    setPostalCode(originalPostalCode);
    setIsEditingPostalCode(false);
    toast.dismiss();
  }

  if (!user) return <p className="p-4">Please login to view your profile.</p>

  return (
    <div>
      <CardBox className="m-2">
        <h5>General Information</h5>
        <div className="overflow-x-auto overflow-y-hidden">
          <Table className="table-fixed w-full">
            <TableBody className="border-0">
              <TableRow>
                <TableCell className="w-48 text-20 font-semibold  py-3 whitespace-nowrap">Username</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{user.username}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="w-48 text-20 font-semibold  py-3 whitespace-nowrap">Postal Code</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      {
                        !isEditingPostalCode ? (
                          <div className="flex items-center">
                            <p className="text-sm text-gray-700">
                              {postalCode ? postalCode : "Not set"}
                            </p>
                            <button
                              onClick={() => setIsEditingPostalCode(true)}
                              className="ml-[50px] bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
                            >
                              Edit Postal Code
                            </button>
                          </div>
                        ) : (
                          <div className="flex items-center">
                            <input
                              id="postalCode"
                              pattern="[0-9]*"
                              disabled={!isEditingPostalCode}
                              value={postalCode}
                              onChange={(e) => setPostalCode(e.target.value)}
                              className={`w-20 border rounded p-2 ${isEditingPostalCode ? 'bg-white' : 'bg-gray-100 text-gray-500'}`}
                            />
                            <div className="flex gap-4 items-center">
                              <button
                                onClick={handleCancelUpdate}
                                className="ml-[50px] bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded"
                              >
                                Cancel
                              </button>
                              <button
                                onClick={handleUpdate}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
                              >
                                Save
                              </button>
                            </div>
                          </div>
                        )
                      }

                    </div>
                  </div>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </CardBox>

      {

        postalCode && <LocationMap postalCode={parseInt(postalCode)} />

      }

    </div>
  )

}

export default UserInfo;