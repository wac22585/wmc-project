<template>
  <v-layout>
    <NavBar />
    <v-main class="d-flex align-center justify-center">
      <v-container class="main">
        <div class="title">Users</div>
        <div class="subtitle">view and manage users</div>
        <div class="add-btns">
          <v-text-field v-model="searchQuery" prepend-inner-icon="mdi-magnify" single-line hide-details label="Search..." variant="outlined" ></v-text-field>
          <v-divider vertical class="mx-2"></v-divider>
        </div>
        <div>
          <v-data-table
            :headers="dynamicHeaders"
            :items="filteredUsers"
            :items-per-page="10"
          >
            <template v-slot:headers>
              <tr>
                <th class="text-left table-head">User</th>
                <th v-if="!isSmallScreen" class="text-left table-head">Created</th>
                <th class="text-left table-head">Roles</th>
                <th class="text-left table-head"></th>
              </tr>
            </template>

            <template v-slot:item.fullname="{ item }">
              <span class="username collapsible-text" :class="{'collapsible-small-screen': colapse}">{{ item.firstname }} {{ item.lastname }}</span>
              <span class="collapsible-text" :class="{'collapsible-small-screen': colapse}">{{ item.email }}</span>
            </template>

            <template v-slot:item.created="{ item }">
              {{ new Date(item.created).toLocaleDateString("de-DE") }}
            </template>

            <template v-slot:item.roles="{ item }">
              <div v-if="!colapse && item.roles && item.roles.length > 0">
                    <v-chip v-if="item.roles[0]">
                      {{ item.roles[0] }}
                    </v-chip>
                    <span v-if="item.roles.length > 1" class="text-grey text-caption">
                      (+{{ item.roles.length - 1 }} other<span v-if="item.roles.length > 2">s</span>)
                    </span>
                  </div>
                  <div v-if="colapse && item.roles && item.roles.length > 0">
                    <v-chip>{{ item.roles[0] }}<span v-if="item.roles.length > 1">, ...</span></v-chip>
                  </div>
            </template>

            <template v-slot:item.actions="{ item, index }">
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
                      <router-link class="router-link" v-bind:to="`/edit/${item.id}`">
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
                        @click="deleteUser(item.id, index)" 
                        />
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
            </template>
          </v-data-table>
        </div>
      </v-container>
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
      searchQuery: '',
      isSmallScreen: false,
      colapse: false,
    };
  },
  computed: {
    dynamicHeaders() {
      let headers = [
        { text: 'User', value: 'fullname' },
        ...(!this.isSmallScreen ? [{ text: 'Created', value: 'created' }] : []),
        { text: 'Roles', value: 'roles' },
        { text: '', value: 'actions', sortable: false, width: '30px' },
      ];
      return headers;
    },
    filteredUsers() {
    if (!this.searchQuery) return this.users;
    const searchLower = this.searchQuery.toLowerCase();

    return this.users.filter(user => {
      const firstnameMatch = user.firstname.toLowerCase().startsWith(searchLower);
      const lastnameMatch = user.lastname.toLowerCase().startsWith(searchLower);
      const emailMatch = user.email.toLowerCase().startsWith(searchLower);
      return firstnameMatch || lastnameMatch || emailMatch;
    });
  }
  },
  async mounted() {
    this.checkScreenSize();
    window.addEventListener('resize', this.checkScreenSize)
    try {
      const response = await axios.get('users/all');
      this.users = response.data;
      this.users.forEach(u => {
        if (u.roles && u.roles.length > 0) {
          u.roles = u.roles.map(role => 
              role.charAt(0).toUpperCase() + role.slice(1).toLowerCase()
            );
        }
      });
      this.dialogs = this.users.map(() => ({ showOuterDialog: false, showInnerDialog: false }));
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
    async deleteUser(id, index) {
      try {
        const response = await axios.put(`users/delete/${id}`);
        if (response.data === 'OK') {
          this.users.splice(index, 1);
          this.dialogs.splice(index, 1);
        } else {
          alert('Error deleting user:', response.data);
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
    toggleDrawer() {
      this.drawer = !this.drawer;
    },
    checkScreenSize() {
      this.isSmallScreen = window.innerWidth < 1280;
      this.colapse = window.innerWidth < 600;
      this.marginWidth = this.isSmallScreen ? '0px' : '70px';
    },
  },
};
</script>

<style scoped>
  .main {
    margin-top: 70px;
    margin-right: 70px;
    padding-inline: 0px !important;
  }
  .title {
    color: black;
    font-size: 30px;
  }

  .subtitle {
    color: black;
    font-size: 20px;
    margin-top: -10px;
    margin-bottom: 20px;
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

  .collapsible-text {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: block;
  }

  .collapsible-small-screen {
    max-width: 90px;
  }
</style>