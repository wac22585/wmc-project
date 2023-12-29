<template>
    <v-container class="outer">
        <div class="inner">
            <button @click="back">
            <v-icon size="40">mdi-arrow-left</v-icon>
            </button>
            <div class="title">New User</div>
            <div class="subtitle">Create a new User</div>
            <v-form @submit-prevent="addUser">
                <v-row>
                    <v-col>
                        <label for="">Firstname</label>
                        <InputField class="inputfield"
                        :model-value="user.firstname" 
                        @update:model-value="newValue => user.firstname = newValue" inputType="text" />
                    </v-col>
                    <v-col>
                        <label for="">Lastname</label>
                        <InputField
                        :model-value="user.lastname" 
                        @update:model-value="newValue => user.lastname = newValue" inputType="text" />
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">E-Mail-Address</label>
                        <InputField class="inputfield" 
                        :model-value="user.email" 
                        @update:model-value="newValue => user.email = newValue" inputType="text" />
                    </v-col>
                    <v-col>
                        <label for="">Phone-Number</label>
                        <InputField></InputField>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">Roles</label>
                        <v-select
                            :items="roles"
                            v-model="selectedRoles"
                            bg-color="none"
                            density="compact"
                            variant="plain"
                            multiple
                            chips
                            class="select"
                        ></v-select>
                    </v-col>
                    <v-col>
                        <label for="">Date of Birth</label>
                        <v-row justify="start">
                            <v-col>
                                <v-select
                                    :items="months"
                                    v-model="dob.month"
                                    bg-color="none"   
                                    density="compact"
                                    variant="plain"
                                    class="select_date"
                                ></v-select>
                            </v-col>
                            <v-col>
                                <v-select
                                :items="days"
                                v-model="dob.day"
                                bg-color="none"
                                density="compact"
                                variant="plain"
                                class="select_date year"
                                ></v-select>
                            </v-col>
                            <v-col>
                                <v-select
                                :items="years"
                                v-model="dob.year"
                                bg-color="none"
                                density="compact"
                                variant="plain"
                                class="select_date year"
                                ></v-select>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">Password</label>
                        <InputField class="inputfield" 
                            width="400px"
                            :model-value="user.password" 
                            @update:model-value="newValue => user.password = newValue" />
                    </v-col>
                    <v-col>
                        <label for="">Confirm Password</label>
                        <InputField 
                            :model-value="confirmpassword" 
                            @update:model-value="newValue => confirmpassword = newValue" />
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <div class="btn-div">
                            <Btn @click="addUser" label="Create" />
                        </div>
                    </v-col>
                </v-row>
            </v-form>
        </div>
    </v-container>
</template>
  
<script>
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import axios from 'axios';
    import LoginView from './LoginView.vue';

    export default {
        data() {
            return {
                user: {},
                confirmpassword: '',
                roles: [],
                selectedRoles: [],
                dob: {
                    month: '',
                    date: '',
                    year: ''
                },
                months: [
                    'January', 'February', 'March', 'April', 'May', 'June',
                    'July', 'August', 'September', 'October', 'November', 'December'
                ],
                years: (() => {
                    const currentYear = new Date().getFullYear();
                    const years = [];
                    for (let i = currentYear; i >= currentYear - 100; i--) {
                    years.push(i);
                    }
                    return years;
                })(),
                days: Array.from({ length: 31 }, (_, i) => i + 1),
            }
        },
        components: {
            InputField,
            Btn,
        },
        async mounted() {
            try {
                const response = await axios.get('roles/all');
                for(const r of response.data) 
                {
                    const name = r.name.charAt(0) + r.name.slice(1).toLowerCase()
                    name.replace("_", " ");
                    const role = {id: r.id, name: name};
                    this.roles.push(name);
                }
                console.log(this.roles)
            } catch (error) {
                console.log('An error occurred: ', error)
            }
        },
        methods: {
            back() {
                this.$router.push({ name: 'Home' });
            },
            async addUser() {
                try {
                    let user = this.user;

                    const formData = new URLSearchParams();
                    formData.append('firstname', this.user.firstname.charAt(0).toUpperCase() + this.user.firstname.slice(1));
                    formData.append('lastname', this.user.lastname.charAt(0).toUpperCase() + this.user.lastname.slice(1));
                    formData.append('email', this.user.email);
                    formData.append('password', this.user.password);
                    formData.append('number', BigInt(0));


                    const response = await axios.post('users/add', formData);

                    if (response.status === 200 && response.data) {
                        console.log(response.data);
                    } else {
                        console.error('Error adding user');
                    }
                } catch (error) {
                    console.error('An error occurred:', error);
                }
            }
        }
    };
</script>

<style scoped>
    .inner {
       display: inline-block;
       margin: auto;
    }

    .outer{
        display: flex;
        align-items: center;
    }

    .title {
        margin-top: 10px;
        color: black;
        font-size: 30px;
    }

    .subtitle {
        color: black;
        font-size: 20px;
        margin-top: -10px;
        margin-bottom: 40px;
    }

    .col-end {
        margin-left: auto;
    }

    .btn-div {
        display: flex;
        justify-content: end;
    }

    .inputfield {
        margin-right: 20px;
    }

    .select_date {
        border: solid 1px #707070;
        border-radius: 7px;
        margin-top: 10px;
        height: 50px;
        padding-inline: 10px;
        width: 150px;
        padding-top: 5px;
    }

    .year {
        width: 100px;
    }

    .select {
        border: #707070 1px solid;
        background: white !important;
        height: 50px;
        width: 400px;
        margin: 10px 0px;
        margin-right: 20px;
        border-radius: 7px;
        padding-inline: 10px;
        padding-top: 5px;
        display: block;
    }
</style>

  