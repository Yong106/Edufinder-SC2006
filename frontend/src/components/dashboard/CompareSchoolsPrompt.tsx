import { useState, useRef,useEffect } from 'react';
import { Navbar, Textarea, Button, Modal, TextInput } from 'flowbite-react';
import { Icon } from '@iconify/react';
import { GoogleGenerativeAI } from "@google/generative-ai";
import ReactMarkdown from 'react-markdown';
import fetchSavedSchoolIds from 'src/utils/fetchSavedSchoolIds.ts';
import toast from 'react-hot-toast';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';
import { Cca, School } from 'src/types/school/school.ts';
import { useAuth } from 'src/context/AuthProvider.tsx';


const CompareSchoolsPrompt = () => {
  const [showAddSchoolModal, setShowAddSchoolModal] = useState(false);
  const [isSchoolOpen, setSchoolOpen] = useState(false);
  const [schoolQuery, setSchoolQuery] = useState('');
  const [selectedSchools, setSelectedSchools] = useState<string[]>([]);
  const [addedSchools, setAddedSchools] = useState<School[]>([]);
  const schoolRef = useRef<HTMLDivElement | null>(null);
  const justOpened = useRef(false);
  const [compareResult, setCompareResult] = useState<JSX.Element | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [userPrompt, setUserPrompt] = useState("");
  const { schoolMap } = useSchoolContext();
  const [savedSchools, setSavedSchools] = useState<School[]>([]);
  const { isLoggedIn } = useAuth();


  useEffect(() => {
    const fetchSavedSchools = async () => {
      try {
        const res = await fetchSavedSchoolIds();
        const savedSchools = res.map(id => schoolMap.get(id)).filter(Boolean)
        setSavedSchools(savedSchools);
      } catch (err) {
        console.error('Error fetching saved schools', err);
        toast.error('Failed to load saved schools');
      }
    };

    if (isLoggedIn) fetchSavedSchools();
  }, []);
  
  const filteredSchools = savedSchools.filter((s) =>
    s.name.toLowerCase().includes(schoolQuery.trim().toLowerCase())
  );
  const orderedFilteredSchools = [
    ...savedSchools.filter((s) => selectedSchools.includes(s.name)),
    ...filteredSchools.filter((s) => !selectedSchools.includes(s.name)),
  ];
  
  const toggleSchoolSelection = (school: string) => {
    setSelectedSchools((s) =>
      s.includes(school) ? s.filter((x) => x !== school) : [...s, school]
    );
  };
  const removeAddedSchool = (name: string) => {
    setAddedSchools((prev) => prev.filter((p) => p.name !== name));
    setSelectedSchools((prev) => prev.filter((n) => n !== name));
  };  

  const apiKey = import.meta.env.VITE_GEMINI_API_KEY;
  const ai = new GoogleGenerativeAI(apiKey||""); 
  const model = ai.getGenerativeModel({ model: "gemini-2.5-flash" });

  const callCompare = async () => {
    if (addedSchools.length < 2) {
      window.alert('Select at least 2 schools to compare.');
      return;
    }
    if (userPrompt==""){
      window.alert('Enter a prompt.');
      return;
    }

    setIsLoading(true);
    setCompareResult(null);

    try {
      var schools = JSON.stringify(addedSchools)
      const result = await model.generateContent(
        "You are a school recommending system. Scan the following schools " + schools + "and suggest the best school for this parent "+userPrompt+ "justifying your choice in 3 sentences. If the user's prompt is insufficient, ask him for more details.  If there is no clear winner, just choose one.");
      const response = await result.response;
      const text = await response.text();
      const final = <ReactMarkdown>{text}</ReactMarkdown>;
      setCompareResult(final);
    } catch (err: any) {
      console.error('Compare failed', err);
      window.alert('Compare failed: ' + (err?.message ?? err));
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (showAddSchoolModal) {
        justOpened.current = true;
        const t = setTimeout(() => (justOpened.current = false), 300);
      return () => clearTimeout(t);
    }
  }, [showAddSchoolModal]);
  useEffect(() => {
    function onDoc(e: MouseEvent) {
      const target = e.target as Node;
      const insideSchool = schoolRef.current?.contains(target);
      if (!insideSchool) {
        setSchoolOpen(false);
      }
    }
    document.addEventListener('mousedown', onDoc);
    return () => document.removeEventListener('mousedown', onDoc);
  }, []);



  return (
    <>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <div className="flex-1 px-4 min-w-0">
          <Navbar fluid className={`rounded-lg bg-white shadow-md  py-4 `}>
            <h5 className="font-semibold text-lg pb-5">Your Prompt:</h5>
            <div className="px-4 w-full">
              <div className="min-w-0">
                <Textarea
                  id="name"
                  placeholder={`Welcome to SmartFinder! Input a prompt to compare the schools that you have selected below. It could be about:
1) Your child's personality, interest and hobbies.
2) Any requirements you have about academic subjects / CCA / location of school`}
                  required
                  className="w-full form-control form-rounded-xl h-30"
                  rows={6}
                  value = {userPrompt}
                  onChange={(e)=>{
                    const v = e.target.value;
                    setUserPrompt(v);
                  }}
                  
                />
              </div>
                  {addedSchools.length > 0 && (
                    <div className="overflow-x-auto mt-4">
                      <table className="table-fixed w-full text-sm">
                        <thead className="bg-gray-50">
                          <tr>
                            <th className="w-20 px-4 py-2 text-center font-medium text-gray-700"></th>
                            {addedSchools.map((s) => (
                              <th key={s.name} className="w-52 px-4 py-2 text-center font-medium text-gray-700 border-l border-gray-200">
                                <div className="flex items-center justify-center">
                                  <div className="font-bold text-lg flex items-center gap-2 justify-center">
                                    <span>{s.name}</span>
                                    <button
                                      type="button"
                                      onClick={() => removeAddedSchool(s.name)}
                                      aria-label={`Remove ${s.name} from compare`}
                                      title={`Remove ${s.name}`}
                                      className="text-red-600 hover:text-red-800 text-sm px-1 py-0.5 rounded inline-flex items-center"
                                    >
                                      <Icon icon="mdi:trash-can" className="text-lg" />
                                    </button>
                                  </div>
                                </div>
                              </th>
                            ))}
                          </tr>
                        </thead>
                        <tbody className="bg-white text-sm">
                          {[
                            { key: 'location', label: 'Location' },
                            { key: 'address', label: 'Address' },
                            { key: 'postalCode', label: 'Postal Code' },
                            { key: 'nearbyMrtStation', label: 'Nearest MRT' },
                            { key: 'nearbyBusStation', label: 'Nearest Bus' },
                            { key: 'type', label: 'School Type' },
                            { key: 'natureCode', label: 'Gender' },
                            { key: 'level', label: 'Level' },
                            { key: 'sessionCode', label: 'Session' },
                            { key: 'sapInd', label: 'SAP School' },
                            { key: 'autonomousInd', label: 'Autonomous' },
                            { key: 'giftedInd', label: 'Gifted Education' },
                            { key: 'ipInd', label: 'Integrated Programme' },
                            { key: 'minCutOffPoint', label: 'Min Cut-off' },
                            { key: 'maxCutOffPoint', label: 'Max Cut-off' },
                            {
                              key: 'ccas',
                              label: 'CCAs',
                              transform: (ccas: Cca[]) =>
                                ccas.length > 0
                                  ? (
                                    <ul className="list-disc list-inside text-left">
                                      {ccas.map((c) => (
                                        <li key={c.name}>{c.name}</li>
                                      ))}
                                    </ul>
                                  )
                                  : '-',
                            },
                            {
                              key: 'subjects',
                              label: 'Subjects',
                              transform: (subs: string[]) =>
                                subs.length > 0
                                  ? (
                                    <ul className="list-disc list-inside text-left">
                                      {subs.map((s) => (
                                        <li key={s}>{s}</li>
                                      ))}
                                    </ul>
                                  )
                                  : '-',
                            },
                            {
                              key: 'programmes',
                              label: 'Programmes',
                              transform: (progs: string[]) =>
                                progs.join(', ') || '-',
                            },
                            { key: 'website', label: 'Website' },
                            { key: 'email', label: 'Email' },
                            { key: 'phoneNumber', label: 'Phone' },
                            { key: 'faxNumber', label: 'Fax' },
                            {
                              key: 'motherTongue1',
                              label: 'Mother Tongue 1',
                            },
                            {
                              key: 'motherTongue2',
                              label: 'Mother Tongue 2',
                            },
                            {
                              key: 'motherTongue3',
                              label: 'Mother Tongue 3',
                            },
                          ].map((row) => (
                            <tr key={row.key} className="border-b border-dotted border-gray-200">
                              <td className="w-20 px-4 py-2 font-medium text-primary text-center">
                                {row.label}
                              </td>
                              {addedSchools.map((s) => {
                                const value = (s as any)[row.key];
                                const display =
                                  row.transform?.(value) ??
                                  (typeof value === 'boolean'
                                    ? value
                                      ? 'Yes'
                                      : 'No'
                                    : value ?? '-');

                                return (
                                  <td
                                    key={`${s.name}-${row.key}`}
                                    className="w-52 px-4 py-2 border-l border-gray-200 text-center break-words"
                                  >
                                    {display}
                                  </td>
                                );
                              })}
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}

                  <div className="flex justify-center mt-4">
                    <button
                      type="button"
                        onClick={() => {
                          justOpened.current = true;
                          setSchoolOpen(false);
                          setSelectedSchools(addedSchools.map((s) => s.name));
                          setShowAddSchoolModal(true);
                        }}
                      className="h-12 w-12 rounded-full bg-primary text-white flex items-center justify-center shadow-lg border-2 border-white"
                      aria-label="Add"
                    >
                      <Icon icon="mdi:plus" className="text-xl" />
                    </button>
                  </div>

                  

              <div className="flex justify-end mt-4">
                <Button
                  type="button"
                  onClick={()=>callCompare()}
                  className="text-white bg-primary hover:bg-primaryemphasis focus:ring-4 focus:outline-none focus:ring-primary/30 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center"
                >
                  Compare
                </Button>
              </div>

              <Modal show={showAddSchoolModal} onClose={() => setShowAddSchoolModal(false)} dismissible>
                <div className="mt-6 px-6 pb-6">
                  <h3 className="font-semibold text-2xl pb-3">Add Saved Schools</h3>
                  <div className="relative inline-block w-full" ref={schoolRef}>
                    <TextInput
                      id="school-search"
                      value={schoolQuery}
                      onChange={(e) => {
                          const v = e.target.value;
                          setSchoolQuery(v);
                          if (v.trim().length > 0) setSchoolOpen(true);
                          else setSchoolOpen(false);
                      }}
                        onFocus={() => { if (!justOpened.current) setSchoolOpen(true); }}
                        placeholder={selectedSchools.length==0?"Search Schools...":selectedSchools.join(', ')}
                        className="w-full pb-5"
                      />
                      <div
                        id="schools-menu"
                        className={`${isSchoolOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                        role="menu"
                        aria-labelledby="school-search"
                      >
                        <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                          {orderedFilteredSchools.length > 0 ? (
                            orderedFilteredSchools.map((s) => (
                            <li key={s.name}>
                              <label className="flex items-center p-2 rounded hover:bg-gray-100">
                                <input
                                  type="checkbox"
                                  checked={selectedSchools.includes(s.name)}
                                  onChange={() => toggleSchoolSelection(s.name)}
                                  className="form-checkbox"
                                />
                                <span className="ml-2">{s.name}</span>
                              </label>
                            </li>
                            ))
                          ) : (
                          <li className="p-2 text-gray-500">No results</li>
                          )}
                        </ul>
                    </div>
                  </div>
                  <div className="flex justify-end gap-3">
                    <Button
                      onClick={() => {
                        setAddedSchools((prev) => {
                          const toAdd = selectedSchools
                            .filter((name) => !prev.some((s) => s.name === name))
                            .map((name) => savedSchools.find((s) => s.name === name))
                            .filter((s): s is School => !!s);

                          return [...prev, ...toAdd];
                        });

                        setSelectedSchools([]);
                        setShowAddSchoolModal(false);
                      }}
                      color="primary"
                    >
                      Add Schools
                    </Button>
                  </div>
                </div>
              </Modal>
            </div>
            </Navbar>                         
        </div>
      </div>
              {isLoading && (
                <div className="mt-6 lg:col-span-8 col-span-12">
                  <div className="flex-1 px-4 min-w-0">
                    <Navbar fluid className={`rounded-lg bg-white shadow-md  py-4 `}>
                      <h5 className="font-semibold text-lg pb-3">Generating…</h5>
                      <div className="px-4 w-full flex items-center gap-3">
                        <Icon icon="mdi:loading" className="text-xl animate-spin" />
                        <div className="text-sm text-gray-600">Generating comparison — this may take a few seconds.</div>
                      </div>
                    </Navbar>
                  </div>
                </div>
              )}

              {compareResult && !isLoading && (
                <div className="mt-6 lg:col-span-8 col-span-12">
                  <div className="flex-1 px-4 min-w-0">
                    <Navbar fluid className={`rounded-lg bg-white shadow-md  py-4 `}>
                      <h5 className="font-semibold text-lg pb-3">School Recommendation</h5>
                      <div className="px-4 w-full">
                        {compareResult}
                      </div>
                    </Navbar>
                  </div>
                </div>
              )}
    </>
  );
};

export default CompareSchoolsPrompt;