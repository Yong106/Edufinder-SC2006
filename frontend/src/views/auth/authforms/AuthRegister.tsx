import { Button, Label, TextInput } from "flowbite-react";
import { useNavigate } from "react-router";
import { FormEvent, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import { useAuth } from 'src/context/AuthProvider.tsx';
import PasswordRequirementsPopup from 'src/views/auth/register/PasswordRequirementsPopup.tsx';


const AuthRegister = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (event:FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {

      if (username.length < 6 || username.length > 14) {
        toast.error("Username must be between 6 and 14 characters long.")
        return;
      }

      const res = await fetch(CONSTANTS.backendEndpoint + '/auth/signup', {
        method: 'POST',
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      })

      if (res.status === 400) {
        toast.error("Password does not meet complexity requirements.");
      } else if (res.status === 409) {
        toast.error("Username already exists.")
      } else if (!res.ok) {
        console.log(await res.json());
        throw new Error('Registration failed.');
      } else {
        toast.success("Registration Successful");

        const data = await res.json();
        console.log(data);

        login({
          id: data.id,
          username: data.username,
          postalCode: data.postalCode ? data.postalCode : "",
        });

        navigate("/");
      }
    } catch (err) {
      console.log("Registration error: ", err);
      toast.error("Registration failed.");
    }

  }

  return (
    <>
      <form onSubmit={handleSubmit} >
        <div className="mb-4">
          <div className="mb-2 block">
            <Label>Username</Label>
          </div>
          <TextInput
            id="name"
            type="text"
            sizing="md"
            required
            className="form-control form-rounded-xl"
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div className="mb-6">
          <div className="mb-2 block">
            <Label>
              Password
              <PasswordRequirementsPopup />
            </Label>
          </div>
          <TextInput
            id="userpwd"
            type="password"
            sizing="md"
            required
            className="form-control form-rounded-xl"
            onChange={(e) => setPassword(e.target.value)}
          />
        </div> 
        <Button color={'primary'} type="submit" className="w-full">Sign Up</Button> 
        
      </form>
    </>
  )
}

export default AuthRegister
