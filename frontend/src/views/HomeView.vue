<template>
  <v-layout>
    <NavBar />
    <v-main class="main">
      <div class="title">Users</div>
      <div class="subtitle">view and manage users</div>
      <div class="add-btns">
        <InputField width="700px" label="Search" class="inputfield"></InputField>
        <v-divider vertical class="mx-2"></v-divider>
        <Button content="Filter" />
        <Button content="Add" @click="add" />
      </div>
      <div>
        <v-table>
          <thead>
            <tr>
              <th class="text-left table-head">User</th>
              <th class="table-head">Created</th>
              <th class="text-left table-head">Role</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(user, index) in users" :key="user.id">
              <td>
                <span class="username">{{ user.firstname }} {{ user.lastname }}</span><br>
                <span>{{ user.email }}</span>
              </td>
              <td>{{ new Date(user.created).toLocaleDateString("de-DE") }}</td>
              <td>
                <div v-if="user.roles && user.roles.length > 0">
                  <v-chip v-if="user.roles[0]">
                    {{ user.roles[0] }}
                  </v-chip>
                  <span v-if="user.roles.length > 1" class="text-grey text-caption">
                    (+{{ user.roles.length - 1 }} others)
                  </span>
                </div>
              </td>
              <td>
                <v-menu
                  v-model="dialogs[index].showOuterDialog"
                  :close-on-content-click="false"
                  location="end"
                >
                  <template v-slot:activator="{ props} ">
                    <button 
                      class="btn-more" 
                      @click="openOutderDialog(index)" 
                      v-bind="props">
                        <svg-icon type="mdi" :path="path"></svg-icon>
                    </button>
                  </template>

                  <v-card>
                    <router-link class="router-link" v-bind:to="`/edit/${user.id}`">
                      <v-btn variant="text" prepend-icon="mdi-pencil" block class="align-start">
                        Edit user
                      </v-btn>
                    </router-link>
                    <v-divider></v-divider>
                      <v-btn variant="text" prepend-icon="mdi-delete" block @click="openInnerDialog(index)">
                        Delete user
                      </v-btn>
                  </v-card>
                </v-menu>
                <v-dialog v-model="dialogs[index].showInnerDialog" width="300">
                  <v-card class="popup-window text-center">
                    <v-card-title>
                      Are you sure?
                    </v-card-title>
                    <v-card-text>
                      This will not permanently delete the user!
                    </v-card-text>
                    <v-card-actions class="confirm-action">
                      <Btn label="Cancel" 
                      @click="closeInnerDialog(index)"  
                      useWhiteBackground="true"/>
                      <Btn label="Delete" 
                      @click="deleteUser(user.id, index)" 
                      />
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </td>
            </tr>
          </tbody>
        </v-table>
      </div>
    </v-main>
  </v-layout>
</template>

<script setup>
import axios from 'axios';
import InputField from '@/components/Input.vue';
import Button from '@/components/Button-Settings.vue';
import Btn from '@/components/Button.vue';
import NavBar from '@/components/Navigation-Bar.vue';
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiDotsHorizontal } from '@mdi/js';
</script>

<script>
export default {
  components: {
    SvgIcon,
    InputField,
    Button,
    Btn,
    NavBar,
  },
  data() {
    return {
      users: [],
      dialogs: [],
      path: mdiDotsHorizontal,
    };
  },
  async mounted() {
    this.checkScreenSize();
    window.addEventListener('resize', this.checkScreenSize)
    try {
      const response = await axios.get('users/all');
      this.users = response.data;
      this.users.forEach(user => {
          if (user.roles && user.roles.length > 0) {
            user.roles = user.roles.map(role => 
                role.charAt(0).toUpperCase() + role.slice(1).toLowerCase()
              );
          }
      });
      this.dialogs = this.users.map(() => ({ showOuterDialog: false, showInnerDialog: false }));
      console.log(this.dialogs)
    } catch (e) {
      alert(e);
    }
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkScreenSize);
  },
  methods: {
    add() {
      this.$router.push({ name: 'Add' });
    },
    async remove(id) {
      // Placeholder for update functionality
      console.log('Update user with id:', id);
    },
    async deleteUser(id, index) {
      try {
        const response = await axios.put(`users/delete/${id}`);
        if (response.data === 'OK') {
          this.users.splice(index, 1);
          this.dialogs.splice(index, 1);
        } else {
          console.error('Error deleting user:', response.data);
        }
      } catch (e) {
        alert(e);
      }
    },
    openOutderDialog(index) {
      this.dialogs[index].showOuterDialog = true;
    },
    openInnerDialog(index) {
      this.dialogs[index].showOuterDialog = false;
      this.dialogs[index].showInnerDialog = true;
    },
    closeInnerDialog(index) {
      this.dialogs[index].showInnerDialog = false;
    },
    async logout() {
      try {
        const token = localStorage.getItem('authToken');
        const response = await axios.put('/users/logout', null, {
          params: {
            authToken: token
          }
        });
        if(response.data) {
          localStorage.removeItem('authToken');
          this.$router.push({name: 'Login'});
        } else {
            console.error('Error deleting user:', response.data);
        }
      } catch(error) {
        alert(error);
      }
    },
    toggleDrawer() {
      this.drawer = !this.drawer;
    },
    checkScreenSize() {
      this.isSmallScreen = window.innerWidth < 1280;
      this.marginWidth = this.isSmallScreen ? '0px' : '70px';
    },
  },
};
</script>

<style scoped>
  .main {
    margin-top: 70px;
  }
  .title {
    color: black;
    font-size: 30px;
  }

  .subtitle {
    color: black;
    font-size: 20px;
    margin-top: -10px;
  }

  .table-head {
    color: #707070;
  }

  .username {
    font-weight: bold;
  }

  .btn-more {
    border-radius: 50%;
    padding: 2px;
  }

  .inputfield {
    display: inline-block;
  }

  .menu {
    border: solid 1px #707070;
    border-radius: 10px;
    margin-right: 10px;
  }

  .action-btn {
    background: white;
    border: 1px black solid;
    margin: 5px;
    border-radius: 20px;
  }

  .delete-btn {
    background: black;
    color: white;
  }

  .popup-window {
    border-radius: 20px !important;
    border: solid 1px #707070;
  }

  .align-start {
    justify-content: flex-start;
    padding-top: 9px;
  }

  .confirm-action {
    justify-content: center;
  }

  .router-link {
    text-decoration: none !important;
    color: #212121;
  }
</style>