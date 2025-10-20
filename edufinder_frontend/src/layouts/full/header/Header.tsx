import { useState, useEffect, useRef } from 'react';
import { Button, Navbar, Label, TextInput, Modal } from 'flowbite-react';
import { Icon } from '@iconify/react';

const Header = () => {
  const [isFilterOpen, setFilterOpen] = useState(false);
  const [isLocOpen, setLocOpen] = useState(false);
  const [locQuery, setLocQuery] = useState('');
  const [selectedLocations, setSelectedLocations] = useState<string[]>([]);
  const [isCCAOpen, setCCAOpen] = useState(false);
  const [ccaQuery, setCCAQuery] = useState('');
  const [selectedCCAs, setSelectedCCAs] = useState<string[]>([]);
  const [isSubjectOpen, setSubjectOpen] = useState(false);
  const [subjectQuery, setSubjectQuery] = useState('');
  const [selectedSubjects, setSelectedSubjects] = useState<string[]>([]);
  const [isNatureCodeOpen, setNatureCodeOpen] = useState(false);
  const [selectedNatureCodes, setSelectedNatureCodes] = useState<string[]>([]);
  const [isSchoolTypeOpen, setSchoolTypeOpen] = useState(false);
  const [selectedSchoolTypes, setSelectedSchoolTypes] = useState<string[]>([]);
  const [isSessionCodeOpen, setSessionCodeOpen] = useState(false);
  const [selectedSessionCodes, setSelectedSessionCodes] = useState<string[]>([]);
  const [minCOP, setMinCOP] = useState('4');
  const [maxCOP, setMaxCOP] = useState('32');
  const locRef = useRef<HTMLDivElement | null>(null);
  const ccaRef = useRef<HTMLDivElement | null>(null);
  const subjectRef = useRef<HTMLDivElement | null>(null);
  const natureCodeRef = useRef<HTMLDivElement | null>(null);
  const schoolTypeRef = useRef<HTMLDivElement | null>(null);
  const sessionCodeRef = useRef<HTMLDivElement | null>(null);
  const justOpened = useRef(false);

  var locations = ['Jurong', 'Serangoon', 'Pasir Panjang']
  var CCAs = ['Basketball', 'Arts & Crafts', 'Bowling']
  var Subjects = ['A Math', 'E Math', 'Chinese']
  var NatureCodes = ['Co-ed', 'Male', 'Female']
  var SchoolTypes = ['Independent','Government School','Goverment-Aided','Special Assistance Plan (SAP)']
  var SessionCodes = ['AM','PM']

  const filteredLocations = locations.filter((l) =>
    l.toLowerCase().includes(locQuery.trim().toLowerCase())
  );
  const filteredCCAs = CCAs.filter((l) =>
    l.toLowerCase().includes(ccaQuery.trim().toLowerCase())
  );
  const filteredSubjects = Subjects.filter((l) =>
    l.toLowerCase().includes(subjectQuery.trim().toLowerCase())
  );
  const orderedFilteredLocations = [
    ...locations.filter((l) => selectedLocations.includes(l)),
    ...filteredLocations.filter((l) => !selectedLocations.includes(l)),
  ];
  const orderedFilteredCCAs = [
    ...CCAs.filter((c) => selectedCCAs.includes(c)),
    ...filteredCCAs.filter((c) => !selectedCCAs.includes(c)),
  ];
  const orderedFilteredSubjects = [
    ...Subjects.filter((s) => selectedSubjects.includes(s)),
    ...filteredSubjects.filter((s) => !selectedSubjects.includes(s)),
  ];
  const orderedNatureCodes = [
    ...NatureCodes.filter((c) => selectedNatureCodes.includes(c)),
    ...NatureCodes.filter((c) => !selectedNatureCodes.includes(c)),
  ];
  const orderedSchoolTypes = [
    ...SchoolTypes.filter((t) => selectedSchoolTypes.includes(t)),
    ...SchoolTypes.filter((t) => !selectedSchoolTypes.includes(t)),
  ];
  const orderedSessionCodes = [
    ...SessionCodes.filter((t) => selectedSessionCodes.includes(t)),
    ...SessionCodes.filter((t) => !selectedSessionCodes.includes(t)),
  ];

  const toggleLocSelection = (loc: string) => {
    setSelectedLocations((s) =>
      s.includes(loc) ? s.filter((x) => x !== loc) : [...s, loc]
    );
  };
  const toggleCCASelection = (cca: string) => {
    setSelectedCCAs((s) =>
      s.includes(cca) ? s.filter((x) => x !== cca) : [...s, cca]
    );
  };
  const toggleSubjectSelection = (subject: string) => {
    setSelectedSubjects((s) =>
      s.includes(subject) ? s.filter((x) => x !== subject) : [...s, subject]
    );
  };
  const toggleNatureCodeSelection = (code: string) => {
    setSelectedNatureCodes((s) =>
      s.includes(code) ? s.filter((x) => x !== code) : [...s, code]
    );
  };
  const toggleSchoolTypeSelection = (type: string) => {
    setSelectedSchoolTypes((s) =>
      s.includes(type) ? s.filter((x) => x !== type) : [...s, type]
    );
  };
  const toggleSessionCodeSelection = (code: string) => {
    setSelectedSessionCodes((s) =>
      s.includes(code) ? s.filter((x) => x !== code) : [...s, code]
    );
  };

  const handleFilter = () => {
    setFilterOpen(!isFilterOpen)
  }

  useEffect(() => {
    if (isFilterOpen) {
      justOpened.current = true;
      const t = setTimeout(() => (justOpened.current = false), 10);
      return () => clearTimeout(t);
    }
  }, [isFilterOpen]);

  useEffect(() => {
    function onDoc(e: MouseEvent) {
      const target = e.target as Node;
      const insideLoc = locRef.current?.contains(target);
      const insideCca = ccaRef.current?.contains(target);
      const insideSubject = subjectRef.current?.contains(target);
      const insideNatureCode = natureCodeRef.current?.contains(target);
      const insideSchoolType = schoolTypeRef.current?.contains(target);
      const insideSessionCode = sessionCodeRef.current?.contains(target);
      if (!insideLoc) {
        setLocOpen(false);
      }
      if (!insideCca) {
        setCCAOpen(false);
      }
      if (!insideSubject) {
        setSubjectOpen(false);
      }
      if (!insideNatureCode) {
        setNatureCodeOpen(false);
      }
      if (!insideSchoolType) {
        setSchoolTypeOpen(false);
      }
      if (!insideSessionCode) {
        setSessionCodeOpen(false);
      }
    }
    document.addEventListener('mousedown', onDoc);
    return () => document.removeEventListener('mousedown', onDoc);
  }, []);

  return (
    <>
      <header>
        <h1 className="font-semibold text-4xl pb-5">Search Schools</h1>
        <Navbar fluid className={`rounded-lg bg-white shadow-md  py-4 `}>
          <div className="flex gap-3 items-center justify-between w-full ">
            <div className="flex gap-2 items-center">
            </div>
            <div className="flex-1 px-4 min-w-0">
              <TextInput
                id="name"
                type="text"
                placeholder="School Name"
                required
                className="w-full form-control form-rounded-xl"
              />
            </div>
            <div className="flex gap-4 items-center">
              <Button
                type="button"
                className="text-white bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                onClick={() => handleFilter()}
              >
                Filter
                <Icon className="ml-2" icon="solar:filter-linear" width={16} height={16} />
              </Button>
            </div>
          </div>
          <div className='pt-3 pl-7 flex flex-wrap items-center gap-2 justify-start w-full'>
            {selectedLocations.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedLocations([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:map-point-wave-linear" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedLocations.length == 1 ? 'Location: ' : 'Locations: '} {selectedLocations.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {selectedCCAs.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedCCAs([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:balls-broken" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedCCAs.length == 1 ? 'CCA: ' : 'CCAs: '} {selectedCCAs.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {selectedSubjects.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedSubjects([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:ruler-pen-linear" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedSubjects.length == 1 ? 'Subject: ' : 'Subjects: '} {selectedSubjects.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {selectedNatureCodes.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedNatureCodes([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:users-group-rounded-bold-duotone" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedNatureCodes.length == 1 ? 'Nature Code: ' : 'Nature Codes: '} {selectedNatureCodes.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {selectedSchoolTypes.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedSchoolTypes([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:square-academic-cap-2-line-duotone" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedSchoolTypes.length == 1 ? 'School Type: ' : 'School Types: '} {selectedSchoolTypes.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {selectedSessionCodes.length > 0 && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => setSelectedSessionCodes([])}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:clock-circle-linear" width={16} height={16} />
                  <span className="whitespace-nowrap">{selectedSessionCodes.length == 1 ? 'Session Code: ' : 'Session Codes: '} {selectedSessionCodes.join(', ')}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
            {!((maxCOP=='32') && minCOP=='4') && (
              <Button
                color="primary"
                className="relative border border-primary text-primary hover:bg-red-100 hover:text-red-600 hover:border-red-600 mx-1 group flex items-center flex-shrink-0"
                pill
                outline
                onClick={() => {
                  setMinCOP('4');
                  setMaxCOP('32');
                }}
              >
                <span className="flex items-center transition-opacity duration-150 opacity-100 group-hover:opacity-0">
                  <Icon className="mr-2" icon="solar:plus-minus-outline" width={16} height={16} />
                  <span className="whitespace-nowrap">Cut-Off Point: {minCOP} - {maxCOP}</span>
                </span>
                <span className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-150 pointer-events-none">
                  ✕ Remove
                </span>
              </Button>
            )}
          </div>
        </Navbar>
      </header>
      <Modal show={isFilterOpen} onClose={() => setFilterOpen(false)} dismissible>
        <div className="mt-6 px-10 pb-10" >
          <h3 className="font-semibold text-2xl pb-3">Filters</h3>
          <div className="grid grid-cols-12 gap-6">
            <div className="lg:col-span-6 col-span-12">
              <div className="flex flex-col gap-4">
                <div className="relative inline-block w-full" ref={locRef}>
                  <div className="mb-2 block">
                    <Label>Locations</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    value={locQuery}
                    onChange={(e) => {
                      setLocQuery(e.target.value);
                      setLocOpen(true);
                    }}
                    onFocus={() => { if (!justOpened.current) setLocOpen(true); }}
                    placeholder={selectedLocations.length==0?"Search Locations...":selectedLocations.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isLocOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedFilteredLocations.length > 0 ? (
                        orderedFilteredLocations.map((location) => (
                          <li key={location}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedLocations.includes(location)}
                                onChange={() => toggleLocSelection(location)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{location}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
                <div className="relative inline-block w-full" ref={ccaRef}>
                  <div className="mb-2 block">
                    <Label>CCAs</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    value={ccaQuery}
                    onChange={(e) => {
                      setCCAQuery(e.target.value);
                      setCCAOpen(true);
                    }}
                    onFocus={() => { if (!justOpened.current) setCCAOpen(true); }}
                    placeholder={selectedCCAs.length==0?"Search CCAs...":selectedCCAs.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isCCAOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedFilteredCCAs.length > 0 ? (
                        orderedFilteredCCAs.map((CCA) => (
                          <li key={CCA}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedCCAs.includes(CCA)}
                                onChange={() => toggleCCASelection(CCA)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{CCA}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
                <div className="relative inline-block w-full" ref={subjectRef}>
                  <div className="mb-2 block">
                    <Label>Subjects</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    value={subjectQuery}
                    onChange={(e) => {
                      setSubjectQuery(e.target.value);
                      setSubjectOpen(true);
                    }}
                    onFocus={() => { if (!justOpened.current) setSubjectOpen(true); }}
                    placeholder={selectedSubjects.length==0?"Search Subjects...":selectedSubjects.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isSubjectOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedFilteredSubjects.length > 0 ? (
                        orderedFilteredSubjects.map((subject) => (
                          <li key={subject}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedSubjects.includes(subject)}
                                onChange={() => toggleSubjectSelection(subject)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{subject}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
                <div>
                  <div className="mb-2 block">
                    <Label>Cut-Off Point Range</Label>
                  </div>
                  <div className="flex items-center">
                    <TextInput
                      id="min-cop"
                      type="number"
                      value={minCOP}
                      onChange={(e) => {
                        setMinCOP(e.target.value); // Store as string
                      }}
                      onBlur={(e) => {
                        const v = parseInt(e.target.value, 10);
                        const newMin = Number.isNaN(v) || v < 4 ? 4 : Math.min(v, 32);
                        setMinCOP(String(newMin));
                        if (newMin > parseInt(maxCOP)) setMaxCOP(String(newMin));
                      }}
                      min={4}
                      max={32}
                      step={1}
                      required
                      className="form-control form-rounded-xl"
                    />
                    <p className="px-3">-</p>
                    <TextInput
                      id="max-cop"
                      type="number"
                      value={maxCOP}
                      onChange={(e) => {
                        setMaxCOP(e.target.value); // Store as string
                      }}
                      onBlur={(e) => {
                        const v = parseInt(e.target.value, 10);
                        const newMax = Number.isNaN(v) || v >32 ? 32 : Math.min(v, 32);
                        setMaxCOP(String(newMax));
                        if (newMax > parseInt(maxCOP)) setMaxCOP(String(newMax));
                      }}
                      min={4}
                      max={32}
                      step={1}
                      required
                      className="form-control form-rounded-xl"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="lg:col-span-6 col-span-12">
              <div className="flex  flex-col gap-4">
                <div className="relative inline-block w-full" ref={natureCodeRef}>
                  <div className="mb-2 block">
                    <Label>Nature Code</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    onFocus={() => { if (!justOpened.current) setNatureCodeOpen(true); }}
                    placeholder={selectedNatureCodes.length==0?"Select Nature Code":selectedNatureCodes.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isNatureCodeOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedNatureCodes.length > 0 ? (
                        orderedNatureCodes.map((code) => (
                          <li key={code}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedNatureCodes.includes(code)}
                                onChange={() => toggleNatureCodeSelection(code)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{code}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
                <div className="relative inline-block w-full" ref={schoolTypeRef}>
                  <div className="mb-2 block">
                    <Label>School Type</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    onFocus={() => { if (!justOpened.current) setSchoolTypeOpen(true); }}
                    placeholder={selectedSchoolTypes.length==0?"Select School Type":selectedSchoolTypes.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isSchoolTypeOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-30 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedSchoolTypes.length > 0 ? (
                        orderedSchoolTypes.map((type) => (
                          <li key={type}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedSchoolTypes.includes(type)}
                                onChange={() => toggleSchoolTypeSelection(type)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{type}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
                <div className="relative inline-block w-full" ref={sessionCodeRef}>
                  <div className="mb-2 block">
                    <Label>Session Code</Label>
                  </div>
                  <TextInput
                    id="location-search"
                    onFocus={() => { if (!justOpened.current) setSessionCodeOpen(true); }}
                    placeholder={selectedSessionCodes.length==0?"Select Session Code":selectedSessionCodes.join(', ')}
                    className="w-full"
                  />
                  <div
                    id="locations-menu"
                    className={`${isSessionCodeOpen ? 'block' : 'hidden'} absolute left-0 mt-2 z-40 w-full max-w-xs bg-white rounded-lg shadow`}
                    role="menu"
                    aria-labelledby="location-search"
                  >
                    <ul className="h-20 px-3 pb-3 overflow-y-auto text-sm text-gray-700">
                      {orderedSessionCodes.length > 0 ? (
                        orderedSessionCodes.map((type) => (
                          <li key={type}>
                            <label className="flex items-center p-2 rounded hover:bg-gray-100">
                              <input
                                type="checkbox"
                                checked={selectedSessionCodes.includes(type)}
                                onChange={() => toggleSessionCodeSelection(type)}
                                className="form-checkbox"
                              />
                              <span className="ml-2">{type}</span>
                            </label>
                          </li>
                        ))
                      ) : (
                        <li className="p-2 text-gray-500">No results</li>
                      )}
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default Header;
