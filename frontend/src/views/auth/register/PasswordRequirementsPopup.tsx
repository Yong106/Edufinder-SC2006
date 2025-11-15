import { Icon } from '@iconify/react';
import { useState } from 'react';

const PasswordRequirementsPopup = () => {
  const [showPasswordComplexityRequirements, setShowPasswordComplexityRequirements] = useState(false);

  return (
    <div className="relative inline-block text-left">
      {/* Info icon */}
      <button
        type="button"
        onClick={() => setShowPasswordComplexityRequirements(!showPasswordComplexityRequirements)}
        className="text-blue-600 hover:text-blue-800 focus:outline-none"
        aria-label="Password complexity info"
      >
        <Icon icon="mdi:information-outline" width={20} height={20} />
      </button>

      {/* Toggleable info box */}
      {showPasswordComplexityRequirements && (
        <div className="absolute z-10 mt-2 w-64 p-3 bg-white border border-gray-300 rounded shadow-md text-xs text-gray-700">
          <p className="font-semibold mb-1">Password must:</p>
          <ul className="list-disc list-inside space-y-1">
            <li>Be at least 8 characters long</li>
            <li>Contain at least 1 lowercase letter</li>
            <li>Contain at least 1 uppercase letter</li>
            <li>Include at least 1 number</li>
            <li>Include at least 1 symbol (e.g. @, *, #)</li>
          </ul>
        </div>
      )}
    </div>
  )
}

export default PasswordRequirementsPopup;