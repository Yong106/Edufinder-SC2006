import { Button, Checkbox, Label, TextInput } from "flowbite-react";
import { Link, useNavigate } from "react-router";
import { FormEvent, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import { useAuth } from 'src/context/AuthProvider.tsx';



const AuthLogin = () => {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const { login } = useAuth();

  const navigate = useNavigate();
  const handleSubmit = async (event:FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/auth/login', {
        method: 'POST',
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      })

      if (!res.ok) {

        if (res.status === 400) {
          toast.error("Wrong username or password");
        } else {
          console.log(await res.json());
          throw new Error('Login failed.');
        }
      } else {
        toast.success("Login Successful");

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
      console.log("Login error: ", err);
      toast.error("Login failed.");
    }

  }

  return (
    <>
      <form onSubmit={handleSubmit} >
        <div className="mb-4">
          <div className="mb-2 block">
            <Label >Username</Label>
          </div>
          <TextInput
            id="username"
            name="username"
            type="text"
            sizing="md"
            required
            className="form-control "
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div className="mb-4">
          <div className="mb-2 block">
             <Label >Password</Label>
          </div>
          <TextInput
            id="password"
            name="password"
            type="password"
            sizing="md"
            required
            className="form-control "
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div className="flex justify-between my-5">
          <div className="flex items-center gap-2">
            <Checkbox id="accept" className="checkbox" />
            <Label
              htmlFor="accept"
              className="opacity-90 font-normal cursor-pointer"
            >
              Remember this Device
            </Label>
          </div>
          <Link to={"/"} className="text-primary text-sm font-medium">
            Forgot Password ?
          </Link>
        </div>
        <Button type="submit" color={"primary"}  className="w-full bg-primary text-white">
          Sign in
        </Button>
      </form>
    </>
  );
};

export default AuthLogin;
